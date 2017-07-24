package com.deady.utils.printer;

public enum ORDERSIDE {

	STORE_SIDE(1, "店铺联"), CUSTOMER_SIDE(2, "客户联");

	private int side;

	private String sideInfo;

	private ORDERSIDE(int side, String sideInfo) {
		this.side = side;
		this.sideInfo = sideInfo;
	}

	public int getSide() {
		return side;
	}

	public String getSideInfo() {
		return sideInfo;
	}
}
