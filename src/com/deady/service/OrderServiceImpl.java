package com.deady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.ItemDAO;
import com.deady.dao.OrderDAO;
import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;
import com.deady.entity.operator.Operator;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private ItemDAO itemDAO;

	@Override
	public void addOrder(Order order) {
		orderDAO.insertOrder(order);
	}

	@Override
	public OrderDto getOrderDtoById(String orderId) {
		Order order = orderDAO.findOrderById(orderId);
		OrderDto dto = new OrderDto(order);
		List<Item> itemList = itemDAO.findItemsByOrderId(orderId);
		dto.setItemList(itemList);
		return dto;
	}

}
