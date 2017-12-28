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
		this.payType = order.getPayType();
		this.address = order.getAddress();
		this.state = order.getState();
	}

	private String cusName;// 客户名称
	private String creationTime;// 订单创建时间
	private String payTypeDesc;// 付款方式说明
	private String orderStateDesc;// 订单状态说明

	public OrderDto() {

	}

	private static final long serialVersionUID = 6802401416029548385L;

	private List<Item> itemList = new ArrayList<Item>();

	public String getPayTypeDesc() {
		switch (Integer.parseInt(payType)) {
		case 1:
			return "现金";
		case 2:
			return "刷卡";
		case 3:
			return "支付宝";
		case 4:
			return "微信";
		case 5:
			return "未付";
		case 6:
			return "月结";
		default:
			return "未知";
		}
	}

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

	public String getOrderStateDesc() {
		switch (Integer.parseInt(state)) {
		case 1:
			return "未付款";
		case 2:
			return "待发货";
		case 3:
			return "欠货";
		case 4:
			return "完成";
		case 9:
			return "已删除";
		default:
			return "未知";
		}
	}

}
