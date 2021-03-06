package com.deady.entity.bill;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

/**
 * 订单中的每一条项目
 * 
 * @author wzwuw
 * 
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -6471236486831014861L;

	@BasicEntityField(length = 20, testValue = "20170719165012XXXXXX")
	private String orderId;// 订单id
	@BasicEntityField(length = 32, testValue = "1063696ff9ed4d18aedef7cec40c9132")
	private String id;// 自身id
	@BasicEntityField(length = 32, testValue = "333")
	private String name;// 商品名称
	@BasicEntityField(length = 32, testValue = "黑色")
	private String color;// 商品名称
	@BasicEntityField(length = 32, testValue = "XL")
	private String size;// 尺寸
	@BasicEntityField(length = 32, testValue = "25.00")
	private String unitPrice;// 单价
	@BasicEntityField(length = 32, testValue = "50")
	private String amount;// 数量
	@BasicEntityField(length = 32, testValue = "1250.00")
	private String price;// 金额
	private String creationTime;// 创建时间

	public Item() {

	}

	public Item(String orderId, String id, String name, String color,
			String size, String unitPrice, String amount, String price) {
		this.orderId = orderId;
		this.id = id;
		this.name = name;
		this.color = color;
		this.size = size;
		this.unitPrice = unitPrice;
		this.amount = amount;
		this.price = price;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

}
