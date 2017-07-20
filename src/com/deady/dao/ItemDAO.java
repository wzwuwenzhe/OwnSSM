package com.deady.dao;

import java.util.List;

import com.deady.entity.bill.Item;

public interface ItemDAO {

	/**
	 * 该方法存在问题 暂时未解决
	 * 
	 * @param itemList
	 */
	void insertItems(List<Item> itemList);

	void insertItem(Item item);

}
