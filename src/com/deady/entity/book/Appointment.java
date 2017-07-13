package com.deady.entity.book;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {

	private static final long serialVersionUID = -7733117937211788357L;

	private long bookId;// 图书ID

	private long studentId;// 学号

	private Date appointTime;// 预约时间

	// 多对一的复合属性
	private Book book;// 图书实体

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String toString() {
		return "Book:" + this.book.toString() + " bookId:" + this.bookId
				+ " stuId:" + this.studentId + " appointTime:"
				+ this.appointTime;
	}
}
