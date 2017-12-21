package com.deady.entity.print;

import java.util.List;

import com.deady.dto.OrderDto;

public class Report {
	private String dateStr;
	private List<OrderDto> orders;

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}

}
