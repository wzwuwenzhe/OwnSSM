package com.deady.entity.book;

import java.io.Serializable;

public class Book implements Serializable {

	private static final long serialVersionUID = -3178079670133210111L;

	private long bookId;// 图书ID

	private String name;// 图书名称

	private int number;// 馆藏数量

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String toString() {
		return "id:" + this.bookId + " name:" + this.name + " number:"
				+ this.number;

	}
}