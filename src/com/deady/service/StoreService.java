package com.deady.service;

import com.deady.entity.store.Store;

public interface StoreService {

	void addStore(Store store);

	Store getStoreById(String storeId);

	void modifyStore4Admin(Store store);

	/**
	 * 向打印机中写入图片信息
	 * 
	 * @param store
	 */
	void uploadPic4Printer(Store store);

	void modifyStore4Owner(Store store);

}
