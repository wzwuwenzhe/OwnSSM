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

	int getStockSizeByFactoryId(String factoryId);

	/**
	 * 根据店铺Id 以及 款号 查询相应的颜色以及大小
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	Stock getColorAndSizeByNameAndStoreId(String name, String storeId);

	/**
	 * 根据款号 和店铺Id 找到款式下的所有颜色以及尺寸
	 * 
	 * @param name
	 * @param storeId
	 * @return
	 */
	Storage getStorageByNameAndStoreId(String name, String storeId);

	/**
	 * 批量入库
	 * 
	 * @param stockList
	 */
	void addStocks(List<Stock> stockList);

}
