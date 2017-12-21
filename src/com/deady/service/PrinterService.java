package com.deady.service;

import java.util.Date;

import com.deady.dto.OrderDto;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;
import com.deady.printer.Device;
import com.deady.utils.printer.ORDERSIDE;

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
			OrderDto orderDto, ORDERSIDE storeSide, Date currentTime,
			boolean isReprint);

}
