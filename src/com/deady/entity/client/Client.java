package com.deady.entity.client;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

/**
 * 客户信息类
 * 
 * @author wzwuw
 * 
 */
public class Client implements Serializable {

	private static final long serialVersionUID = -9021680819625798133L;

	@BasicEntityField(length = 20, testValue = "王二麻子")
	private String name; // 客户名称
	@BasicEntityField(length = 13, testValue = "18868808242")
	private String phone;// 客户电话
	@BasicEntityField(length = 32, testValue = "")
	private String id;// 客户id
	private String storeId;// 店铺Id
	private String deliverAddress;// 送货地址

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

}
