package com.deady.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;

public class OrderDto extends Order {

	public OrderDto(Order order) {
		this.id = order.getId();
		this.cusId = order.getCusId();
		this.operatorId = order.getOperatorId();
		this.storeId = order.getStoreId();
		this.smallCount = order.getSmallCount();
		this.discount = order.getDiscount();
		this.totalAmount = order.getTotalAmount();
		this.remark = order.getRemark();
		this.creationTime = order.getCreationTime();
	}

	private String cusName;// 客户名称
	private String creationTime;// 订单创建时间

	public OrderDto() {

	}

	private static final long serialVersionUID = 6802401416029548385L;

	private List<Item> itemList = new ArrayList<Item>();

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

}
