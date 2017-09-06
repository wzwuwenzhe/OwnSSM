package com.deady.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.deady.entity.stock.Stock;
import com.deady.entity.stock.Storage;

public interface StockDAO {

	void insertStock(Stock stock);

	List<Storage> findStorageByStoreId(String storeId);

	void updateStorage(Storage storage);

	void insertStorage(Storage storage);

	List<Stock> findStockListByYearAndNameArr(Map<String, Object> params);

	void deleteStorage(@Param("name") String name, @Param("year") String year,
			@Param("storeId") String storeId);

	void deleteStock(@Param("name") String name, @Param("year") String year,
			@Param("storeId") String storeId);

	int findStocksByFactoryId(String factoryId);

}
