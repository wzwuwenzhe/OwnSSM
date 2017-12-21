package com.deady.action.remote;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.dto.OrderDto;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;
import com.deady.service.OrderService;
import com.deady.service.PrinterService;
import com.deady.utils.SpringContextUtil;
import com.deady.utils.printer.ORDERSIDE;
import com.google.gson.Gson;

@Controller
public class RemoteTaskAction {

	@Autowired
	private PrinterService printerService;

	private static Logger logger = LoggerFactory
			.getLogger(RemoteTaskAction.class);
	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");

	@RequestMapping(value = "/remotePrint", method = RequestMethod.POST)
	@DeadyAction(checkReferer = true, checkLogin = false)
	@ResponseBody
	public Object doRemoteBillPrint(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		String orderJsonStr = req.getParameter("order");
		String storeJsonStr = req.getParameter("store");
		String clientJsonStr = req.getParameter("client");
		String privateKey = req.getParameter("privateKey");
		String isReprint = req.getParameter("isReprint");
		logger.info("orderJsonStr:" + orderJsonStr);
		logger.info("storeJsonStr:" + storeJsonStr);
		logger.info("clientJsonStr:" + clientJsonStr);
		logger.info("privateKey:" + privateKey);
		String localeKey = config.getString("private.key");
		if (!localeKey.equals(privateKey)) {
			response.setSuccess(false);
			response.setMessage("密钥错误!");
			return response;
		}
		Gson gson = new Gson();
		OrderDto orderDto = gson.fromJson(orderJsonStr, OrderDto.class);
		Store store = gson.fromJson(storeJsonStr, Store.class);
		Client client = gson.fromJson(clientJsonStr, Client.class);
		Device device = null;
		try {
			device = new Device();
			DeviceParameters params = new DeviceParameters();
			device.setDeviceParameters(params);
			device.openDevice();
			Date currentTime = new Date();
			// 打印店铺联
			printerService.printOrder(device, store, client, orderDto,
					ORDERSIDE.STORE_SIDE, currentTime,
					isReprint.equals("1") ? true : false);
			device.printString("");
			device.printString("");
			// // 打印客户联
			printerService.printOrder(device, store, client, orderDto,
					ORDERSIDE.CUSTOMER_SIDE, currentTime,
					isReprint.equals("1") ? true : false);

		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("打印出错!:" + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			// 关闭设备 释放内存
			device.closeDevice();
		}
		response.setSuccess(true);
		response.setMessage("打印成功");
		return response;
	}
}
