package com.deady.dao;

import com.deady.entity.bill.Order;

public interface OrderDAO {

	void insertOrder(Order order);

	Order findOrderById(String orderId);

}
