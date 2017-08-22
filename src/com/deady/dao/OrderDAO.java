package com.deady.dao;

import java.util.List;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;
import com.deady.entity.order.OrderSearchEntity;

public interface OrderDAO {

	void insertOrder(Order order);

	Order findOrderById(String orderId);

	List<OrderDto> findOrderByCondition(OrderSearchEntity orderSearch);

	void deleteOrderById(String orderId);

}
