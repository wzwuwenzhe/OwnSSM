package com.deady.dao;

import java.util.List;
import java.util.Map;

import com.deady.entity.stock.Stock;
import com.deady.entity.stock.Storage;

public interface StockDAO {

	void insertStock(Stock stock);

	List<Storage> findStorageByStoreId(String storeId);

	void updateStorage(Storage storage);

	void insertStorage(Storage storage);

	List<Stock> findStockListByYearAndNameArr(Map<String, Object> params);

	void deleteStorage(String name, String year, String storeId);

	void deleteStock(String name, String year, String storeId);

}
