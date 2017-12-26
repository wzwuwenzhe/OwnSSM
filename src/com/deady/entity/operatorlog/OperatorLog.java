package com.deady.entity.operatorlog;

public class OperatorLog {

	private String storeId;// 店铺Id
	private String operatorId;// 操作员Id
	private String operatorType;// 操作员类型
	private String operatorName;// 操作员名字
	private String requestUrl;// 请求URL
	private String params;// 请求参数
	private String operateTime;// 操作时间

	public OperatorLog() {

	}

	public OperatorLog(String storeId, String operatorId, String operatorType,
			String operatorName, String requestUrl, String params,
			String operateTime) {
		this.storeId = storeId;
		this.operatorId = operatorId;
		this.operatorType = operatorType;
		this.operatorName = operatorName;
		this.requestUrl = requestUrl;
		this.params = params;
		this.operateTime = operateTime;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

}
