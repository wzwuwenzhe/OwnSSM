package com.deady.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;
import com.deady.entity.order.OrderSearchEntity;

public interface OrderDAO {

	void insertOrder(Order order);

	Order findOrderById(String orderId);

	List<OrderDto> findOrderByCondition(OrderSearchEntity orderSearch);

	void deleteOrderById(String orderId);

	void updateOrderPayTypeById(@Param("orderId") String orderId,
			@Param("payType") String payType);

	void updateOrderStateById(@Param("orderId") String orderId,
			@Param("state") String state);

	void updateOrderRemarkById(@Param("orderId") String orderId,
			@Param("remark") String remark);

}
