package com.deady.common;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
	private boolean success;// 状态
	private int code;// 状态返回码
	private String message;// 返回提示信息
	private int total;// 数据总长度
	private String data;// json格式

	public String toString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", this.success);
		map.put("code", this.code);
		map.put("message", this.message);
		map.put("total", this.total);
		map.put("data", this.data);
		return map.toString();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
