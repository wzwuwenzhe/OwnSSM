package com.deady.utils.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.service.OrderService;
import com.deady.utils.HttpClientUtil;
import com.deady.utils.SpringContextUtil;

public class RemoteReportTask extends Task {

	private static Logger logger = LoggerFactory
			.getLogger(RemoteReportTask.class);
	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");
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
			Map<String, Object> dataMap = orderService.printReport(beginDate,
					endDate, storeId);
			String clientUrl = config.getString("remote.url");
			logger.info("报表发送的数据:" + dataMap.toString());
			String back = HttpClientUtil.sendPost(clientUrl + "/remoteReport",
					dataMap, "UTF-8");
			if (!StringUtils.isEmpty(back)) {
				logger.info("请求返回信息:" + back);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
