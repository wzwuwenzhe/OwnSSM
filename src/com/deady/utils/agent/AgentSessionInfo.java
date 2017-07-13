package com.deady.utils.agent;

import java.io.Serializable;

/**
 * @author Andre.Z 2014-11-5 下午3:07:17<br>
 * 
 */
@SuppressWarnings("serial")
public class AgentSessionInfo implements Serializable {

	private String agentCode = null;// 代理商号码 13
	private String agentName = null;// 代理商姓名 20
	private String orgcode = null;// 代理商机构号 10
	private String loginNum = null;// 登录次数 1
	private String smsFlag = null;// 是否发送短信 1
	private String rePay = null;// 返钱记录条数 3
	private String agentBalance = null;// 代理商本金余额 16
	private String provinceOrgcode = null;// 代理商省际机构号 10
	private String integral = null;// 积分 16
	private String address = null;// 配送地址 100
	private String contactPhone = null;// 代理商联系电话 20
	private String contact = null;// 联系人 20
	private String agtLevel = null;// 代理级别，2，1为1代，2为代，etc..
	private String bhjNotice = null; // 1年期备货金到期记录条数 3

	private boolean lockScreen = false;

	public String getAgentCode() {
		return this.agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getLoginNum() {
		return this.loginNum;
	}

	public void setLoginNum(String loginNum) {
		this.loginNum = loginNum;
	}

	public String getSmsFlag() {
		return this.smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getRePay() {
		return this.rePay;
	}

	public void setRePay(String rePay) {
		this.rePay = rePay;
	}

	public String getAgentBalance() {
		return this.agentBalance;
	}

	public void setAgentBalance(String agentBalance) {
		this.agentBalance = agentBalance;
	}

	public String getProvinceOrgcode() {
		return this.provinceOrgcode;
	}

	public void setProvinceOrgcode(String provinceOrgcode) {
		this.provinceOrgcode = provinceOrgcode;
	}

	public String getIntegral() {
		return this.integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAgtLevel() {
		return this.agtLevel;
	}

	public void setAgtLevel(String agtLevel) {
		this.agtLevel = agtLevel;
	}

	public String getBhjNotice() {
		return this.bhjNotice;
	}

	public void setBhjNotice(String bhjNotice) {
		this.bhjNotice = bhjNotice;
	}

	public boolean isLockScreen() {
		return this.lockScreen;
	}

	public void setLockScreen(boolean lockScreen) {
		this.lockScreen = lockScreen;
	}

}
