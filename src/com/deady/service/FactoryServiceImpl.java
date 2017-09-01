package com.deady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.FactoryDAO;
import com.deady.entity.factory.Factory;

@Service
public class FactoryServiceImpl implements FactoryService {

	@Autowired
	private FactoryDAO factoryDAO;

	@Override
	public List<Factory> getFactoryListByStoreId(String storeId) {
		return factoryDAO.findFactoryListByStoreId(storeId);
	}

	@Override
	public Factory getFactoryById(String factoryId) {
		return factoryDAO.findFactoryById(factoryId);
	}

	@Override
	public void addFactory(Factory factory) {
		factoryDAO.insertFactory(factory);
	}

	@Override
	public void removeFactoryById(String factoryId) {
		factoryDAO.deleteFactory(factoryId);
	}

	@Override
	public void modifyFactory(Factory factory) {
		factoryDAO.updateFactory(factory);
	}

}
