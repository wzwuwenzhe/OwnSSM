package com.deady.enums;

public enum SizeEnum {
	S(5), M(4), L(3), XL(2), XXL(1);

	private int size;

	private SizeEnum(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
