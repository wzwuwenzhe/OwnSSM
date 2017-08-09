package com.deady.entity.order;

import com.deady.entity.BasicEntityField;

public class OrderSearchEntity {

	@BasicEntityField(length = 32, testValue = "333")
	private String orderName;// 商品名称
	@BasicEntityField(length = 20, testValue = "3629b32d820d44b9ba5b43704018988e")
	private String orderId;// 订单编号
	@BasicEntityField(length = 20, testValue = "王二麻子")
	private String cusName;// 客户姓名
	@BasicEntityField(length = 8, testValue = "05779999")
	private String operatorId;// 操作员id
	@BasicEntityField(length = 8, testValue = "20170801")
	private String beginDate;// 查询开始时间
	@BasicEntityField(length = 8, testValue = "20170801")
	private String endDate;// 查询结束时间

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
}
