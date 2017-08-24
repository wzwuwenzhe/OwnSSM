package com.deady.action.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.deady.service.ClientService;
import com.deady.service.ItemService;
import com.deady.service.OrderService;
import com.deady.service.StoreService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

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
			String[] name, String[] size, String[] unitPrice, String[] amount,
			String[] price) throws Exception {
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(req.getParameter("cusId"))) {
			response.setSuccess(false);
			response.setMessage("请先选择客户");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		Order order = new Order();
		ActionUtil.assObjByRequest(req, order);
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
		for (int i = 0; i < name.length; i++) {
			// 后期考虑加验证 每一项都需要验证
			Item item = new Item();
			item.setName(name[i]);
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
			orderService.printOrder(orderId, op, false);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
		response.setSuccess(true);
		response.setMessage("订单添加成功!");
		response.setData("/index");
		return response;
	}

}
