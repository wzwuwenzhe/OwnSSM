package com.deady.entity.operatorlog;

import com.deady.entity.BasicEntityField;

public class OperatorLogSearchEntity {

	@BasicEntityField(length = 10, testValue = "0571888822")
	private String operatorId;// 操作员姓名
	@BasicEntityField(length = 50, testValue = "333")
	private String operatorName;// 操作员姓名
	@BasicEntityField(length = 8, testValue = "05718888")
	private String storeId;// 店铺Id
	@BasicEntityField(length = 14, testValue = "20171221201543")
	private String beginDate;// 搜索开始时间
	@BasicEntityField(length = 14, testValue = "20171221201550")
	private String endDate;// 搜索结束时间
	private int start;// 分页开始
	private int pagesize;// 每页显示的数量

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
