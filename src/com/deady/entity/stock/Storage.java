package com.deady.entity.stock;

import java.util.List;

/**
 * 店铺大库存
 * 
 */
public class Storage {

	private String storeId; // 店铺Id
	private String year;// 年份
	private String name;// 款号
	private String total;// 合计
	private String stockLeft;// 剩余库存
	private List<Stock> stockList;// 入库记录

	public Storage() {
	}

	public Storage(String storeId, String year, String name, String total,
			String stockLeft) {
		this.storeId = storeId;
		this.year = year;
		this.name = name;
		this.total = total;
		this.stockLeft = stockLeft;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getStockLeft() {
		return stockLeft;
	}

	public void setStockLeft(String stockLeft) {
		this.stockLeft = stockLeft;
	}

	public List<Stock> getStockList() {
		return stockList;
	}

	public void setStockList(List<Stock> stockList) {
		this.stockList = stockList;
	}

}
