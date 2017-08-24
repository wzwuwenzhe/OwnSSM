package com.deady.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;

public interface OrderService {

	void addOrder(Order order);

	OrderDto getOrderDtoById(String orderId);

	List<OrderDto> getOrderDtoByCondition(OrderSearchEntity orderSearch);

	void printOrder(String orderId, Operator op, boolean isRePrint)
			throws Exception;

	void removeOrder(String orderId);

	/**
	 * 打印报表
	 * 
	 * @param startDateStr
	 *            开始时间
	 * @param endDateStr
	 *            结束时间
	 */
	boolean printReport(String startDateStr, String endDateStr,
			HttpServletRequest req) throws UnsupportedEncodingException;

}
