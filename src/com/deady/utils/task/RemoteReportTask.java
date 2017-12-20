package com.deady.utils.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deady.service.OrderService;
import com.deady.utils.SpringContextUtil;

public class RemoteReportTask extends Task {

	private static Logger logger = LoggerFactory
			.getLogger(RemoteReportTask.class);

	private String beginDate;// 开始时间
	private String endDate;// 结束时间
	private String storeId;// 店铺id

	public RemoteReportTask(String beginDate, String endDate, String storeId) {
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.storeId = storeId;
	}

	@Override
	public void execute() throws Exception {
		try {
			OrderService orderService = (OrderService) SpringContextUtil
					.getBeanByClass(OrderService.class);
			orderService.printReport(beginDate, endDate, storeId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
