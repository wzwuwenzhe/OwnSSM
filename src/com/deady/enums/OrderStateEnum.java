package com.deady.enums;

public enum OrderStateEnum {
	NOTPAY(1, "未付款"), WAITDELIVER(2, "等待发货"), OWEGOODS(3, "欠货"), COMPELETE(4,
			"完成");

	private int state;

	private String orderStateInfo;

	private OrderStateEnum(int state, String orderStateInfo) {
		this.state = state;
		this.orderStateInfo = orderStateInfo;
	}

	public static OrderStateEnum typeOf(int index) {
		for (OrderStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

	public String getOrderStateInfo() {
		return orderStateInfo;
	}

	public int getState() {
		return state;
	}

}
