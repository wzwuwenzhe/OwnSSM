package com.deady.dao;

import java.util.List;

import com.deady.entity.bill.Item;

public interface ItemDAO {

	void insertItems(List<Item> itemList);

	List<Item> findItemsByOrderId(String orderId);

	void deleItemsByOrderId(String orderId);

	void insertReturnItems(List<Item> returnItemList);

	List<Item> findReturnItemsByOrderId(String orderId);

}
