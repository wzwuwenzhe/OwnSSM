package com.deady.action.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.client.Client;
import com.deady.entity.order.OrderSearchEntity;
import com.deady.utils.ActionUtil;

@Controller
public class OrderAction {

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object showOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("/order/order");
	}

	@RequestMapping(value = "/orderSearch", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	public Object searchOrder(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		ActionUtil.assObjByRequest(req, orderSearch);
		req.setAttribute("entity", orderSearch);
		return new ModelAndView("/order/order");
	}
}
