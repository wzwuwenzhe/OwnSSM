package com.deady.entity.bill;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

/**
 * 订单详情
 * 
 * @author wzwuw
 * 
 */
public class Order implements Serializable {

	private static final long serialVersionUID = -8301945175397999355L;
	@BasicEntityField(length = 20, testValue = "20170719165012XXXXXX")
	protected String id;// 订单号
	@BasicEntityField(length = 32, testValue = "3629b32d820d44b9ba5b43704018988e")
	protected String cusId;// 客户id
	@BasicEntityField(length = 32, testValue = "ec409db7f6aa41feab30d134afaeecb0")
	protected String operatorId;// 操作员id
	@BasicEntityField(length = 8, testValue = "05719999")
	protected String storeId;// 店铺id
	@BasicEntityField(length = 20, testValue = "1234.5")
	protected String smallCount;// 小计
	@BasicEntityField(length = 20, testValue = "0")
	protected String discount;// 折扣金额
	@BasicEntityField(length = 20, testValue = "1234.5")
	protected String totalAmount;// 应付总金额
	@BasicEntityField(length = 100, testValue = "我是一个备注")
	protected String remark;// 备注
	protected String creationTime;// 创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSmallCount() {
		return smallCount;
	}

	public void setSmallCount(String smallCount) {
		this.smallCount = smallCount;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

}
