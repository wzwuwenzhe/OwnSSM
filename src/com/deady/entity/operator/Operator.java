package com.deady.entity.operator;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

public class Operator implements Serializable {

	private static final long serialVersionUID = 7434476991512911322L;

	private String id;// 操作员ID 由店铺Id加两位数字组成
	@BasicEntityField(length = 8, testValue = "05779999")
	private String storeId;// 店铺Id
	@BasicEntityField(length = 20, testValue = "吴文哲")
	private String name;// 操作员姓名
	@BasicEntityField(length = 13, testValue = "13867723932")
	private String phone;// 操作员手机号码
	@BasicEntityField(length = 1, testValue = "1")
	private String userType;// 操作员类型 1:超管 ,2:营业员 , 3:店主
	@BasicEntityField(length = 20, testValue = "wuwz")
	private String loginName;// 登录名
	@BasicEntityField(length = 20, testValue = "123321")
	private String pwd;// 密码
	@BasicEntityField(length = 20, testValue = "123321")
	private String rePwd;// 确认密码

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRePwd() {
		return rePwd;
	}

	public void setRePwd(String rePwd) {
		this.rePwd = rePwd;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
