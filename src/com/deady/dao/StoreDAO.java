package com.deady.dao;

import com.deady.entity.store.Store;

public interface StoreDAO {

	void insertStore(Store store);

	Store findStoreById(String storeId);

	void updateStore(Store store);

}
