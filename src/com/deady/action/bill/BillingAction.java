package com.deady.action.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;
import com.deady.entity.client.Client;
import com.deady.entity.operator.Operator;
import com.deady.entity.stock.Storage;
import com.deady.enums.OrderStateEnum;
import com.deady.enums.PayTypeEnum;
import com.deady.service.ClientService;
import com.deady.service.ItemService;
import com.deady.service.OrderService;
import com.deady.service.StockService;
import com.deady.service.StoreService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.task.BillTask;
import com.deady.utils.task.Task;

@Controller
public class BillingAction {

	private static final String SUFFIX = " ";

	@Autowired
	private ClientService clientService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/billing", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object billing(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Client> clientList = clientService.getClientListByStoreId(op
				.getStoreId());
		req.setAttribute("clientList", clientList);
		return new ModelAndView("/bill/billing");
	}

	@RequestMapping(value = "/billing", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doBilling(HttpServletRequest req, HttpServletResponse res,
			String[] name, String[] color, String[] size, String[] unitPrice,
			String[] amount, String[] price) throws Exception {
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(req.getParameter("cusName"))) {
			response.setSuccess(false);
			response.setMessage("请先填写客户姓名");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		Order order = new Order();
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(now);
		String orderId = dateString
				+ UUID.randomUUID().toString().replaceAll("-", "")
						.substring(0, 6).toUpperCase();
		order.setId(orderId);
		order.setOperatorId(op.getId());
		order.setStoreId(op.getStoreId());
		List<Item> itemList = new ArrayList<Item>();
		if (null == name || name.length <= 0 || StringUtils.isEmpty(name)) {
			response.setSuccess(false);
			response.setMessage("请填写订单内容");
			return response;
		}
		// 如果为新客户 就需要先添加客户
		String cusId = null;
		if (StringUtils.isEmpty(req.getParameter("cusId"))) {
			Client c = null;
			// 根据客户名称找一下客户 如果存在就使用之前的客户 如果不存在就新建
			List<Client> existsClients = clientService
					.getClientsByNameAndStoreId(req.getParameter("cusName"),
							op.getStoreId());
			if (null != existsClients && existsClients.size() > 0) {
				c = existsClients.get(0);
				cusId = c.getId();
				// 如果送货地址有变动 则更新送货地址
				String oldDeliverAddress = c.getDeliverAddress();
				String newDeliverAddress = req.getParameter("address");
				if (!oldDeliverAddress.equals(newDeliverAddress)) {
					clientService.updateClientAddressById(c.getId(),
							newDeliverAddress);
				}
			} else {
				c = new Client();
				cusId = UUID.randomUUID().toString().replaceAll("-", "");
				c.setId(cusId);
				c.setName(req.getParameter("cusName"));
				c.setStoreId(op.getStoreId());
				clientService.addClient(c);
			}
		}

		ActionUtil.assObjByRequest(req, order);
		// 根据下单时的付款方式 选择订单状态
		if (order.getPayType().equals(PayTypeEnum.NOTPAY.getType() + "")) {// 未付款
			order.setState(OrderStateEnum.NOTPAY.getState() + "");// 未付款
		} else {
			order.setState(OrderStateEnum.WAITDELIVER.getState() + "");// 待发货
		}
		if (null == order.getCusId() && null != cusId) {
			order.setCusId(cusId);
		}
		// 下单的订单中 款号对应数量map
		Map<String, String> name2AmountMap = new HashMap<String, String>();
		for (int i = 0; i < name.length; i++) {
			String tampAmount = name2AmountMap.get(name[i]);
			if (null == tampAmount) {
				name2AmountMap.put(name[i], amount[i]);
			} else {
				name2AmountMap.put(
						name[i],
						(Integer.parseInt(amount[i]) + Integer
								.parseInt(tampAmount)) + "");
			}
			// 后期考虑加验证 每一项都需要验证
			Item item = new Item();
			item.setName(name[i]);
			item.setColor(color[i]);
			item.setSize(size[i]);
			item.setUnitPrice(unitPrice[i]);
			item.setAmount(amount[i]);
			item.setPrice(price[i]);
			item.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			item.setOrderId(orderId);
			itemList.add(item);
		}
		itemService.addItem(itemList);
		orderService.addOrder(order);
		// 发送打印订单请求
		try {
			// 将打印的操作放到线程中执行 TODO
			// orderService.printOrder(orderId, op, false);
			Task task = new BillTask(orderId, req, false);
			Thread thread = new Thread(task);
			thread.start();
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
		// 减库存
		String year = ActionUtil.getLunarCalendarYear();
		// 店铺中所有的库存
		List<Storage> storageList = stockService.getStorageByStoreId(op
				.getStoreId());
		// 款号对应库存
		Map<String, Storage> name2StorageMap = new HashMap<String, Storage>();
		for (Storage storage : storageList) {
			Storage s = name2StorageMap.get(storage.getName());
			if (null == s) {
				name2StorageMap.put(storage.getName(), storage);
			}
		}
		for (Map.Entry<String, String> entry : name2AmountMap.entrySet()) {
			String kuanhao = entry.getKey();
			String allAmount = entry.getValue();
			Storage oldStorage = name2StorageMap.get(kuanhao);
			if (null != oldStorage) {
				String stockLeft = oldStorage.getStockLeft();
				Storage storage = new Storage(op.getStoreId(), year, kuanhao,
						oldStorage.getTotal(),
						(Integer.parseInt(stockLeft) - Integer
								.parseInt(allAmount)) + "",
						oldStorage.getColors(), oldStorage.getSizes());
				stockService.updateStorage(storage);
			}
		}

		response.setSuccess(true);
		response.setMessage("订单正在打印!");
		response.setData("/index");
		return response;
	}

	@RequestMapping(value = "/getClientNameList", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	public Object getClientNameList(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		return response;
	}

}
