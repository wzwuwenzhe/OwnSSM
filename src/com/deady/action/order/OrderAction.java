package com.deady.action.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.dto.OrderDto;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;
import com.deady.service.OperatorService;
import com.deady.service.OrderService;
import com.deady.utils.ActionUtil;
import com.deady.utils.DateUtils;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class OrderAction {

	@Autowired
	OrderService orderService;
	@Autowired
	OperatorService operatorService;

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@DeadyAction(createToken = true, checkLogin = true)
	public Object showOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator operator = OperatorSessionInfo.getOperator(req);
		req.setAttribute("userType", operator.getUserType());
		return new ModelAndView("/order/order");
	}

	@RequestMapping(value = "/orderSearch", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	public Object searchOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		ActionUtil.assObjByRequest(req, orderSearch);
		if (StringUtils.isEmpty(orderSearch.getBeginDate())
				|| StringUtils.isEmpty(orderSearch.getEndDate())) {
			req.setAttribute("success", "0");
			req.setAttribute("msg", "查询时间都不能为空!");
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
		orderService.printOrder(orderId, op, true);
		response.setSuccess(true);
		response.setMessage("重新打印成功!");
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
		orderService.removeOrder(orderId);
		response.setSuccess(true);
		response.setMessage("订单删除成功!");
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
		if (startDate.getTime() > endDate.getTime()) {
			response.setSuccess(false);
			response.setMessage("结束时间不能早于开始时间!");
			return response;
		}
		boolean isEmpty = orderService.printReport(startDateStr, endDateStr,
				req);
		if (isEmpty) {
			response.setSuccess(false);
			response.setMessage("没有找到可以打印的订单!");
			return response;
		}
		response.setSuccess(true);
		response.setMessage("报表打印成功!");
		return response;
	}
}
