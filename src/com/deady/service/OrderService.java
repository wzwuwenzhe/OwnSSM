package com.deady.service;

import java.util.List;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;

public interface OrderService {

	void addOrder(Order order);

	OrderDto getOrderDtoById(String orderId);

	List<OrderDto> getOrderDtoByCondition(OrderSearchEntity orderSearch);

	void printOrder(String orderId, Operator op) throws Exception;

	void removeOrder(String orderId);

}
