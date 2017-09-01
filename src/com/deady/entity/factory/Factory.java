package com.deady.entity.factory;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

public class Factory implements Serializable {

	private static final long serialVersionUID = 8334842445434702717L;

	@BasicEntityField(length = 32, testValue = "")
	private String id;// 工厂id
	private String storeId;// 店铺Id
	@BasicEntityField(length = 20, testValue = "王二麻子")
	private String name; // 工厂名称
	@BasicEntityField(length = 100, testValue = "工厂地址")
	private String address; // 工厂地址
	@BasicEntityField(length = 13, testValue = "18868808242")
	private String phone;// 工厂电话

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
