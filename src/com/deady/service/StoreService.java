package com.deady.service;

import com.deady.entity.store.Store;

public interface StoreService {

	void addStore(Store store);

	Store getStoreById(String storeId);

	void modifyStore(Store store);

}
