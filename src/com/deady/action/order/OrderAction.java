package com.deady.action.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;
import com.deady.service.OperatorService;
import com.deady.service.OrderService;
import com.deady.utils.ActionUtil;
import com.deady.utils.DateUtils;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.task.RemoteBillTask;
import com.deady.utils.task.RemoteReportTask;
import com.deady.utils.task.Task;

@Controller
public class OrderAction {

	private static Logger logger = org.slf4j.LoggerFactory
			.getLogger(OrderAction.class);

	@Autowired
	OrderService orderService;
	@Autowired
	OperatorService operatorService;

	@RequestMapping(value = "/orderSearch", method = RequestMethod.GET)
	@DeadyAction(createToken = true, checkLogin = true)
	public Object showOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator operator = OperatorSessionInfo.getOperator(req);
		req.setAttribute("userType", operator.getUserType());
		return new ModelAndView("/order/order");
	}

	@RequestMapping(value = "/orderSearch", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkLogin = true)
	public Object doSearchOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		ActionUtil.assObjByRequest(req, orderSearch);
		if (StringUtils.isEmpty(orderSearch.getBeginDate())
				|| StringUtils.isEmpty(orderSearch.getEndDate())) {
			req.setAttribute("success", "0");
			req.setAttribute("msg", "查询时间都不能为空!");
			return new ModelAndView("/order/order");
		}
		Date startDate = DateUtils.convert2Date(orderSearch.getBeginDate(),
				"yyyyMMdd");
		Date endDate = DateUtils
				.addDays(DateUtils.convert2Date(orderSearch.getEndDate(),
						"yyyyMMdd"), 1);
		if (startDate.getTime() >= endDate.getTime()) {
			req.setAttribute("success", "0");
			req.setAttribute("msg", "结束时间不能早于开始时间!");
			return new ModelAndView("/order/order");
		}
		String oldEndDateStr = orderSearch.getEndDate();
		// 将结束时间自动加一天
		Date nextDate = DateUtils
				.addDays(DateUtils.convert2Date(orderSearch.getEndDate(),
						"yyyyMMdd"), 1);
		orderSearch.setEndDate(DateUtils.convert2String(nextDate, "yyyyMMdd"));
		Operator operator = OperatorSessionInfo.getOperator(req);
		// 如果有传storeId值进来 就说明是管理员身份
		if (StringUtils.isEmpty(orderSearch.getStoreId())) {
			orderSearch.setStoreId(operator.getStoreId());
		}
		List<OrderDto> orderList = orderService
				.getOrderDtoByCondition(orderSearch);
		orderSearch.setEndDate(oldEndDateStr);
		req.setAttribute("entity", orderSearch);
		req.setAttribute("success", "1");
		req.setAttribute("orderList", orderList);
		req.setAttribute("userType", operator.getUserType());
		return new ModelAndView("/order/order");
	}

	@RequestMapping(value = "/payForTheMoney", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object payForTheMoney(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		String orderId = req.getParameter("orderId");// 订单编号
		String payType = req.getParameter("payType");// 支付类型
		OrderDto order = orderService.getOrderDtoById(orderId);
		if (null == order) {
			response.setSuccess(false);
			response.setMessage("无法查询到订单!");
			return response;
		}
		orderService.modifyOrderPayTypeByOrderId(orderId, payType);
		response.setSuccess(true);
		response.setMessage("付款方式更新成功!");
		return response;

	}

	@RequestMapping(value = "/getOrderById", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object getOrderById(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		String orderId = req.getParameter("orderId");// 订单编号
		OrderDto order = orderService.getOrderDtoById(orderId);
		if (null == order) {
			response.setSuccess(false);
			response.setMessage("无法查询到订单!");
			return response;
		}
		List<Item> itemList = order.getItemList();
		if (null == itemList || itemList.size() == 0) {
			response.setSuccess(false);
			response.setMessage("订单查询失败!");
			return response;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Object> _itemList = new ArrayList<Object>();
		for (Item item : itemList) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("name", item.getName());
			itemMap.put("size", item.getSize());
			itemMap.put("color", item.getColor());
			itemMap.put("unitPrice", item.getUnitPrice());
			itemMap.put("amount", item.getAmount());
			itemMap.put("price", item.getPrice());
			_itemList.add(itemMap);
		}
		resultMap.put("items", _itemList);
		resultMap.put("address", StringUtils.isEmpty(order.getAddress()) ? ""
				: order.getAddress());
		resultMap.put("remark", StringUtils.isEmpty(order.getRemark()) ? ""
				: order.getRemark());
		response.setSuccess(true);
		response.setMessage("订单信息获取成功!");
		response.setData(resultMap);
		return response;

	}

	@RequestMapping(value = "/deliverGoods", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object doDeliverGoods(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		String isOweGoods = req.getParameter("isOweGoods");// 是否欠货
		String oweGoodsRemarkText = req.getParameter("oweGoodsRemarkText");// 欠货备注信息
		String orderId = req.getParameter("orderId");// 订单编号
		OrderDto order = orderService.getOrderDtoById(orderId);
		if (null == order) {
			response.setSuccess(false);
			response.setMessage("无法查询到订单!");
			return response;
		}
		if (isOweGoods.equals("1")) {// 欠货
			orderService.modifyOrderStateById(orderId, "3");
			orderService.modifyOrderRemarkById(
					orderId,
					(StringUtils.isEmpty(order.getRemark()) ? "" : order
							.getRemark()) + "   发货备注：" + oweGoodsRemarkText);

		} else if (isOweGoods.equals("0")) {// 正常发货
			orderService.modifyOrderStateById(orderId, "4");
		}
		response.setSuccess(true);
		response.setMessage("发货成功!");
		return response;

	}

	@RequestMapping(value = "/rePrint", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object rePrintOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String orderId = req.getParameter("orderId");
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(orderId)) {
			response.setSuccess(false);
			response.setMessage("订单编号不能为空!");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		try {
			Task task = new RemoteBillTask(orderId, op.getId(),
					op.getStoreId(), true);
			Thread thread = new Thread(task);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setSuccess(true);
		response.setMessage("正在重新打印!");
		return response;
	}

	@RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object deleteOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String orderId = req.getParameter("orderId");
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(orderId)) {
			response.setSuccess(false);
			response.setMessage("订单编号不能为空!");
			return response;
		}
		// 删除订单不能真正的把订单删除 而是把订单做一个状态修改 改成不可见
		orderService.removeOrder(orderId);
		response.setSuccess(true);
		response.setMessage("订单删除成功!");
		return response;
	}

	@RequestMapping(value = "/searchReport", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object searchReport(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String startDateStr = req.getParameter("beginDate");
		String endDateStr = req.getParameter("endDate");
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(startDateStr)
				|| StringUtils.isEmpty(endDateStr)) {
			response.setSuccess(false);
			response.setMessage("开始时间或者结束时间不能为空");
			return response;
		}
		Date startDate = DateUtils.convert2Date(startDateStr, "yyyyMMdd");
		Date endDate = DateUtils.addDays(
				DateUtils.convert2Date(endDateStr, "yyyyMMdd"), 1);
		if (startDate.getTime() >= endDate.getTime()) {
			response.setSuccess(false);
			response.setMessage("结束时间不能早于开始时间!");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Map<String, Object>> resultList = orderService.searchReport(
				startDateStr, endDateStr, op.getStoreId());
		if (resultList.size() == 0) {
			response.setSuccess(false);
			response.setMessage("没有报表信息");
			return response;
		}
		logger.info(resultList.toString());
		response.setSuccess(true);
		response.setData(resultList);
		return response;
	}

	@RequestMapping(value = "/printReport", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object printReport(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String startDateStr = req.getParameter("beginDate");
		String endDateStr = req.getParameter("endDate");
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(startDateStr)
				|| StringUtils.isEmpty(endDateStr)) {
			response.setSuccess(false);
			response.setMessage("开始时间或者结束时间不能为空");
			return response;
		}
		Date startDate = DateUtils.convert2Date(startDateStr, "yyyyMMdd");
		Date endDate = DateUtils.addDays(
				DateUtils.convert2Date(endDateStr, "yyyyMMdd"), 1);
		if (startDate.getTime() >= endDate.getTime()) {
			response.setSuccess(false);
			response.setMessage("结束时间不能早于开始时间!");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		try {
			Task task = new RemoteReportTask(startDateStr, endDateStr,
					op.getStoreId());
			Thread thread = new Thread(task);
			thread.start();
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}

		response.setSuccess(true);
		response.setMessage("报表正在成功!");
		return response;
	}
}
