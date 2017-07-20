package com.deady.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.OrderDAO;
import com.deady.entity.bill.Order;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Override
	public void addOrder(Order order) {
		orderDAO.insertOrder(order);
	}

}
