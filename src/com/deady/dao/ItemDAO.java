package com.deady.dao;

import java.util.List;

import com.deady.entity.bill.Item;

public interface ItemDAO {

	void insertItems(List<Item> itemList);

	void insertItem(Item item);

	List<Item> findItemsByOrderId(String orderId);

}
