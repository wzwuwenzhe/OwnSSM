package com.deady.entity.operator;

public enum UserType {

	// 1:超管 ,2:营业员 , 3:店主
	Admin(1), Seller(2), StoreOwner(3);

	private int value = 0;

	private UserType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
