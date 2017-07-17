package com.deady.entity.store;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

public class Store implements Serializable {

	private static final long serialVersionUID = -2202975745279648252L;

	@BasicEntityField(length = 8, testValue = "05779999")
	private String id;// 店铺Id
	@BasicEntityField(length = 50, testValue = "我是一个小小店")
	private String name;// 店铺名称
	@BasicEntityField(length = 100, testValue = "我弟家在东北,松花江上啊啊")
	private String address;// 店铺地址
	@BasicEntityField(length = 20, testValue = "057188912865")
	private String telePhone;// 固定电话
	@BasicEntityField(length = 13, testValue = "13867723932")
	private String mobilePhone;// 手机
	@BasicEntityField(length = 100, testValue = "我是一个温馨小提示啦啦啦")
	private String reminder;// 小票上的温馨提示
	@BasicEntityField(length = 100, testValue = "c:/temp/ImgUpload/05719999/1000.png")
	private String logoImg;// logo图片的路径
	@BasicEntityField(length = 100, testValue = "c:/temp/ImgUpload/05719999/1001.png")
	private String wxAddImg;// 微信添加好友的二维码路径
	@BasicEntityField(length = 100, testValue = "c:/temp/ImgUpload/05719999/1002.png")
	private String wxPayImg;// 微信付款二维码路径
	@BasicEntityField(length = 100, testValue = "c:/temp/ImgUpload/05719999/1003.png")
	private String zfbPayImg;// 支付宝付款二维码路径

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getWxAddImg() {
		return wxAddImg;
	}

	public void setWxAddImg(String wxAddImg) {
		this.wxAddImg = wxAddImg;
	}

	public String getWxPayImg() {
		return wxPayImg;
	}

	public void setWxPayImg(String wxPayImg) {
		this.wxPayImg = wxPayImg;
	}

	public String getZfbPayImg() {
		return zfbPayImg;
	}

	public void setZfbPayImg(String zfbPayImg) {
		this.zfbPayImg = zfbPayImg;
	}

}
