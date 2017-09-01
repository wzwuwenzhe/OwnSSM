package com.deady.service;

import java.util.List;

import com.deady.entity.stock.Stock;
import com.deady.entity.stock.Storage;

public interface StockService {

	void addStock(Stock stock);

	List<Storage> getStorageByStoreId(String storeId);

	void updateStorage(Storage storage);

	void addStorage(Storage storage);

	/**
	 * 根据店铺id获取库存信息 额外包含入库记录信息
	 * 
	 * @param storeId
	 * @return
	 */
	List<Storage> getStorageDtoByStoreId(String storeId);

	void removeStorage(String name, String year, String storeId);

}
