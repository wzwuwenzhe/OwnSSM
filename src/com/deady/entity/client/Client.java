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
	private String cusName; // 客户名称
	@BasicEntityField(length = 13, testValue = "18868808242")
	private String cusPhone;// 客户电话

	private String id;// 客户id
	private String storeId;// 店铺Id

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

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

}
