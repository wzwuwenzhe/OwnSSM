package com.deady.utils.task;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deady.entity.operator.Operator;
import com.deady.service.OrderService;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.SpringContextUtil;

public class BillTask extends Task {
	private static Logger logger = LoggerFactory.getLogger(BillTask.class);

	private HttpServletRequest request;
	private String orderId;
	private boolean isReprint = false;

	public BillTask(String orderId, HttpServletRequest request,
			boolean isReprint) {
		this.request = request;
		this.orderId = orderId;
		this.isReprint = isReprint;
	}

	@Override
	public void execute() throws Exception {
		Operator op = OperatorSessionInfo.getOperator(request);
		try {
			OrderService orderService = (OrderService) SpringContextUtil
					.getBeanByClass(OrderService.class);
			orderService.printOrder(orderId, op, isReprint);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
