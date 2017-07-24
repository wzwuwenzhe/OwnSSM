package com.deady.service;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Order;

public interface OrderService {

	void addOrder(Order order);

	OrderDto getOrderDtoById(String orderId);

}
