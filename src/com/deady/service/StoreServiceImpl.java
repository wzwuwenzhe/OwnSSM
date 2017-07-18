package com.deady.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.StoreDAO;
import com.deady.entity.store.Store;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private StoreDAO storeDAO;

	@Override
	public void addStore(Store store) {
		storeDAO.insertStore(store.getId(), store.getName(),
				store.getAddress(), store.getTelePhone(),
				store.getMobilePhone(), store.getReminder(),
				store.getLogoImg(), store.getWxAddImg(), store.getWxPayImg(),
				store.getZfbPayImg());
	}

	@Override
	public Store getStoreById(String storeId) {
		return storeDAO.findStoreById(storeId);
	}

	@Override
	public void modifyStore(Store store) {
		storeDAO.updateStore(store.getId(), store.getName(),
				store.getAddress(), store.getTelePhone(),
				store.getMobilePhone(), store.getReminder(),
				store.getLogoImg(), store.getWxAddImg(), store.getWxPayImg(),
				store.getZfbPayImg(), Integer.parseInt(store.getStatus()));
	}
}
