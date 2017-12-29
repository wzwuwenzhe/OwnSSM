package com.deady.service;

import java.util.List;

import com.deady.entity.bill.Item;

public interface ItemService {

	void addItem(List<Item> itemList);

	void addReturnItem(List<Item> returnItemList);

}
