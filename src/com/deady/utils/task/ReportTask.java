package com.deady.utils.task;

import javax.servlet.http.HttpServletRequest;

import com.deady.service.OrderService;
import com.deady.utils.SpringContextUtil;

public class ReportTask extends Task {

	private String startTime;
	private String endTime;
	private HttpServletRequest request;

	public ReportTask(String startTime, String endTime,
			HttpServletRequest request) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.request = request;
	}

	@Override
	public void execute() throws Exception {
		OrderService orderService = (OrderService) SpringContextUtil
				.getBeanByClass(OrderService.class);
		boolean isEmpty = orderService.printReport(startTime, endTime, request);
		if (isEmpty) {
			throw new Exception("没有可打印的订单");
		}
	}

}
