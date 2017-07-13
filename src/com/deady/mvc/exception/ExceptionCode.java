package com.deady.mvc.exception;

public enum ExceptionCode {

	NotLogin("20000000", "用户未登录"), AccessDeny("20000001", "请求访问受限"), InvalidRequest(
			"20000002", "非法请求"), Alert("30000000", "警告信息"), BrowserDeny(
			"20000002", "不允许访问的浏览器");

	private String code = null;
	private String desc = null;

	private ExceptionCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
