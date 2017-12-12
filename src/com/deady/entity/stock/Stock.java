package com.deady.entity.stock;

import java.io.Serializable;

import com.deady.entity.BasicEntityField;

/**
 * 单次入库的库存
 * 
 * @author wzwuw
 * 
 */
public class Stock implements Serializable {

	private static final long serialVersionUID = 8334842445434702117L;

	private String storeId;// 店铺Id
	private String year;// 年份(以农历的年作为单位,保存在配置文件中)
	@BasicEntityField(length = 20, testValue = "335")
	private String name; // 款号名称
	@BasicEntityField(length = 32, testValue = "1234123asdfad234asdfasd213")
	private String factoryId; // 工厂id
	@BasicEntityField(length = 10, testValue = "100")
	private String amount; // 入库数量
	private String creationTime;// 入库时间
	private String sumForOneFactory;// 同款式下工厂的总数
	private String factoryName;
	@BasicEntityField(length = 100, testValue = "黑色")
	private String color;// 款式颜色
	@BasicEntityField(length = 100, testValue = "M")
	private String size;// 款式尺寸

	public Stock() {
		// 默认构造器
	}

	public Stock(String color, String size, String amount, String storeId,
			String year, String name, String factoryId) {
		this.color = color;
		this.size = size;
		this.amount = amount;
		this.storeId = storeId;
		this.year = year;
		this.name = name;
		this.factoryId = factoryId;
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

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getSumForOneFactory() {
		return sumForOneFactory;
	}

	public void setSumForOneFactory(String sumForOneFactory) {
		this.sumForOneFactory = sumForOneFactory;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
