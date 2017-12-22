package com.deady.service;

import java.util.Date;
import java.util.List;

import com.deady.dto.OrderDto;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;
import com.deady.enums.OrderSideEnum;
import com.deady.printer.Device;

public interface PrinterService {

	/**
	 * 远端打印方法
	 * 
	 * @param device
	 * @param store
	 * @param client
	 * @param orderDto
	 * @param storeSide
	 * @param currentTime
	 * @param isReprint
	 *            是否重新打印
	 */
	void printOrder(Device device, Store store, Client client,
			OrderDto orderDto, OrderSideEnum storeSide, Date currentTime,
			boolean isReprint);

	/**
	 * 打印一天的报表
	 * 
	 * @param device
	 * @param dateStr
	 * @param records
	 */
	void printRecordForOneDay(Device device, String dateStr,
			List<OrderDto> records) throws Exception;

}
