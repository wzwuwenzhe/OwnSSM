package com.deady.enums;

public enum PayTypeEnum {
	CASH(1, "现金"), CARD(2, "刷卡"), ZFB(3, "支付宝"), WEIXIN(4, "微信"), NOTPAY(5,
			"未付款"), MONTHPAY(6, "月付");

	private int type;

	private String payTypeInfo;

	private PayTypeEnum(int type, String payTypeInfo) {
		this.type = type;
		this.payTypeInfo = payTypeInfo;
	}

	public String getPayTypeInfo() {
		return payTypeInfo;
	}

	public static PayTypeEnum typeOf(int index) {
		for (PayTypeEnum type : values()) {
			if (type.getType() == index) {
				return type;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

}
