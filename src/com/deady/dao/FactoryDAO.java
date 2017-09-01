package com.deady.dao;

import java.util.List;

import com.deady.entity.factory.Factory;

public interface FactoryDAO {

	List<Factory> findFactoryListByStoreId(String storeId);

	Factory findFactoryById(String factoryId);

	void insertFactory(Factory factory);

	void deleteFactory(String factoryId);

	void updateFactory(Factory factory);

}
