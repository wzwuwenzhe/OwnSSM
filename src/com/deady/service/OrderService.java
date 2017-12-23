package com.deady.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;
import com.deady.entity.order.OrderSearchEntity;

public interface OrderService {

	void addOrder(Order order);

	OrderDto getOrderDtoById(String orderId);

	List<OrderDto> getOrderDtoByCondition(OrderSearchEntity orderSearch);

	/**
	 * 删除订单的同时 需要把库存加回来 不真正删除数据 只是做状态标记 已删除 state=9
	 * 
	 * @param orderId
	 */
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

	/**
	 * 修改订单支付状态
	 * 
	 * @param orderId
	 * @param payType
	 */
	void modifyOrderPayTypeByOrderId(String orderId, String payType);

	/**
	 * 根据订单编号 修改订单状态
	 * 
	 * @param orderId
	 * @param string
	 */
	void modifyOrderStateById(String orderId, String state);

	/**
	 * 根据订单编号 修改备注信息
	 * 
	 * @param orderId
	 * @param string
	 */
	void modifyOrderRemarkById(String orderId, String string);

	/**
	 * 远程调用打印订单方法
	 * 
	 * @param orderId
	 * @param operatorId
	 * @param storeId
	 * @param isRePrint
	 */
	void printOrder(String orderId, String operatorId, String storeId,
			boolean isRePrint);

	/**
	 * 远程调用打印报表方法
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param storeId
	 */

	Map<String, Object> printReport(String beginDate, String endDate,
			String storeId);

	/**
	 * 查询当天的报表
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> searchReport(String startDateStr,
			String endDateStr, String storeId);

}
