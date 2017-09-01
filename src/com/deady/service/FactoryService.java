package com.deady.service;

import java.util.List;

import com.deady.entity.factory.Factory;

public interface FactoryService {

	/**
	 * 找到店铺下所有的工厂
	 * 
	 * @param storeId
	 * @return
	 */
	List<Factory> getFactoryListByStoreId(String storeId);

	Factory getFactoryById(String factoryId);

	void addFactory(Factory factory);

	void removeFactoryById(String factoryId);

	void modifyFactory(Factory factory);

}
