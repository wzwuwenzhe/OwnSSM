package com.deady.action.bill;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.dto.OrderDto;
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
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.task.RemoteBillTask;
import com.deady.utils.task.Task;

@Controller
public class ReturnGoodsAction {

	private static Logger logger = LoggerFactory
			.getLogger(ReturnGoodsAction.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/returnGoods", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object showReturnGoods(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String orderId = req.getParameter("orderId");
		if (StringUtils.isEmpty(orderId)) {
			return new ModelAndView("/order/order");
		}
		OrderDto order = orderService.getOrderDtoById(orderId);
		if (null == order) {
			return new ModelAndView("/order/order");
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Client> clientList = clientService.getClientListByStoreId(op
				.getStoreId());

		List<String> nameList = orderService.getSalesVolumeTopByCondition(op
				.getStoreId());
		req.setAttribute("clientList", clientList);
		req.setAttribute("nameList", nameList);
		req.setAttribute("order", order);
		return new ModelAndView("/bill/returnGoods");
	}

	@RequestMapping(value = "/returnGoods", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doReturnGoods(HttpServletRequest req,
			HttpServletResponse res, String[] name, String[] color,
			String[] size, String[] unitPrice, String[] amount, String[] price,
			String[] oldName, String[] oldColor, String[] oldSize,
			String[] oldAmount, String[] oldUnitPrice, String[] returnNumber)
			throws Exception {
		FormResponse response = new FormResponse(req);
		// 退款金额
		String returnMoney = req.getParameter("returnMoney");
		if (StringUtils.isEmpty(returnMoney)
				|| Integer.parseInt(returnMoney) < 0) {
			response.setSuccess(false);
			response.setMessage("请输入退货数量进行退货");
			return response;
		}
		// 需要进行退款的订单号
		String returnOrderId = req.getParameter("returnOrderId");
		if (StringUtils.isEmpty(returnOrderId)) {
			response.setSuccess(false);
			response.setMessage("退款订单号错误!");
			return response;
		}
		// 退款款号对应退款数量
		Map<String, String> name2ReturnNumberMap = new HashMap<String, String>();
		// 退货条目明细
		List<Item> returnItemList = new ArrayList<Item>();
		for (int index = 0; index < oldName.length; index++) {
			String _oldName = oldName[index];
			String _oldColor = oldColor[index];
			String _oldSize = oldSize[index];
			String _oldAmount = oldAmount[index];
			String _oldUnitPrice = oldUnitPrice[index];
			String _returnNumber = returnNumber[index];
			if (StringUtils.isEmpty(_returnNumber)
					|| Integer.parseInt(_returnNumber) <= 0) {
				continue;// 如果退货数量小于等于0 就跳过
			}
			if (Integer.parseInt(_oldAmount) < Integer.parseInt(_returnNumber)) {
				response.setSuccess(false);
				response.setMessage("退货数量不能超过原数量");
				return response;
			}
			String tempNumber = name2ReturnNumberMap.get(oldName[index]);
			if (null == tempNumber) {
				name2ReturnNumberMap.put(oldName[index], returnNumber[index]);
			} else {
				name2ReturnNumberMap.put(
						oldName[index],
						(Integer.parseInt(returnNumber[index]) + Integer
								.parseInt(tempNumber)) + "");
			}
			double returnUnitPrice = Integer.parseInt(_returnNumber)
					* Double.parseDouble(_oldUnitPrice);
			String _id = UUID.randomUUID().toString().replaceAll("-", "");
			Item _item = new Item(returnOrderId, _id, _oldName, _oldColor,
					_oldSize, _oldUnitPrice, _returnNumber, new DecimalFormat(
							"#.00").format(returnUnitPrice));
			returnItemList.add(_item);
		}
		// 换货又重新生成订单
		if (StringUtils.isEmpty(req.getParameter("cusName"))) {
			response.setSuccess(false);
			response.setMessage("请先填写客户姓名");
			return response;
		}
		if (null == size || null == color || size.length == 0
				|| color.length == 0) {
			response.setSuccess(false);
			response.setMessage("请点击\"新增新款\"来加单!");
			return response;
		}
		for (String _color : color) {
			if (StringUtils.isEmpty(_color)) {
				response.setSuccess(false);
				response.setMessage("请选择颜色");
				return response;
			}
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		Order order = new Order();
		String orderId = ActionUtil.newOrderId();
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
		String newDeliverAddress = req.getParameter("address");
		String cusName = req.getParameter("cusName");
		String storeId = op.getStoreId();
		Client c = null;

		// 根据客户名称找一下客户 如果存在就使用之前的客户 如果不存在就新建
		List<Client> existsClients = clientService.getClientsByNameAndStoreId(
				cusName, storeId);
		c = existsClients.get(0);
		cusId = c.getId();
		// 如果送货地址有变动 则更新送货地址
		String oldDeliverAddress = c.getDeliverAddress();
		// 老地址存在的话 比较是否相当
		if ((null != oldDeliverAddress && !oldDeliverAddress
				.equals(newDeliverAddress)) || null == oldDeliverAddress) {
			clientService.updateClientAddressById(c.getId(), newDeliverAddress);
			logger.info("updateOldAddress!!");
		}
		ActionUtil.assObjByRequest(req, order);
		// 根据下单时的付款方式 选择订单状态
		if (order.getPayType().equals(PayTypeEnum.NOTPAY.getType() + "")) {// 未付款
			order.setState(OrderStateEnum.NOTPAY.getState() + "");// 未付款
		} else {
			order.setState(OrderStateEnum.REFUND.getState() + "");// 退换货
		}
		if (null == order.getCusId() && null != cusId) {
			order.setCusId(cusId);
		}
		// 下单的订单中 款号对应数量map
		Map<String, String> name2AmountMap = new HashMap<String, String>();
		for (int i = 0; i < name.length; i++) {
			if (StringUtils.isEmpty(size[i])) {
				continue;
			}
			if (amount[i].equals("")) {
				continue;
			}
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
			double _unitPrice = Double.parseDouble(unitPrice[i]);
			int _amount = Integer.parseInt(amount[i]);
			item.setPrice((_unitPrice * _amount) + "");
			item.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			item.setOrderId(orderId);
			itemList.add(item);
		}

		itemService.addItem(itemList);
		itemService.addReturnItem(returnItemList);
		orderService.addOrder(order);
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
		// 减库存
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
		// 加上退款中除去的库存
		for (Map.Entry<String, String> entry : name2ReturnNumberMap.entrySet()) {
			String kuanhao = entry.getKey();
			String allAmount = entry.getValue();
			Storage oldStorage = name2StorageMap.get(kuanhao);
			if (null != oldStorage) {
				String stockLeft = oldStorage.getStockLeft();
				Storage storage = new Storage(op.getStoreId(), year, kuanhao,
						oldStorage.getTotal(),
						(Integer.parseInt(stockLeft) + Integer
								.parseInt(allAmount)) + "",
						oldStorage.getColors(), oldStorage.getSizes());
				stockService.updateStorage(storage);
			}
		}

		// 发送打印订单请求
		try {
			Task task = new RemoteBillTask(orderId, op.getId(),
					op.getStoreId(), false);
			Thread thread = new Thread(task);
			thread.start();
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}

		response.setSuccess(true);
		response.setMessage("订单正在打印!");
		response.setData("/index");
		return response;
	}
}
