package com.deady.utils.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deady.service.OrderService;
import com.deady.utils.SpringContextUtil;

public class RemoteBillTask extends Task {

	private static Logger logger = LoggerFactory
			.getLogger(RemoteBillTask.class);

	private String operatorId;// 操作员Id
	private String storeId;// 店铺Id
	private boolean isRePrint;// 是否为重新打印
	private String orderId;// 订单编号

	public RemoteBillTask(String orderId, String operatorId, String storeId,
			boolean isRePrint) {
		this.orderId = orderId;
		this.operatorId = operatorId;
		this.storeId = storeId;
		this.isRePrint = isRePrint;
	}

	@Override
	public void execute() throws Exception {
		try {
			OrderService orderService = (OrderService) SpringContextUtil
					.getBeanByClass(OrderService.class);
			orderService.printOrder(orderId, operatorId, storeId, isRePrint);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
