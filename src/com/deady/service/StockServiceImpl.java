package com.deady.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.FactoryDAO;
import com.deady.dao.StockDAO;
import com.deady.entity.factory.Factory;
import com.deady.entity.stock.Stock;
import com.deady.entity.stock.Storage;
import com.deady.utils.ActionUtil;
import com.deady.utils.DateUtils;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockDAO stockDAO;
	@Autowired
	private FactoryDAO factoryDAO;

	@Override
	public List<Storage> getStorageByStoreId(String storeId) {
		return stockDAO.findStorageByStoreId(storeId);
	}

	@Override
	public void updateStorage(Storage storage) {
		stockDAO.updateStorage(storage);
	}

	@Override
	public void addStorage(Storage storage) {
		stockDAO.insertStorage(storage);
	}

	@Override
	public List<Storage> getStorageDtoByStoreId(String storeId) {
		List<Storage> list = getStorageByStoreId(storeId);
		String year = com.deady.utils.ActionUtil.getLunarCalendarYear();
		Set<String> nameSet = new HashSet<String>();
		for (Storage storage : list) {
			nameSet.add(storage.getName());
		}
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("year", year);
		params.put("names", nameSet.toArray(new String[0]));
		if (nameSet.size() == 0) {
			return new ArrayList<Storage>();
		}
		List<Stock> stockList = stockDAO.findStockListByYearAndNameArr(params);
		Map<String, String> factoryId2NameMap = new HashMap<String, String>();
		List<Factory> storeFactoryList = factoryDAO
				.findFactoryListByStoreId(storeId);
		for (Factory factory : storeFactoryList) {
			String name = factoryId2NameMap.get(factory.getId());
			if (null == name) {
				factoryId2NameMap.put(factory.getId(), factory.getName());
			}
		}
		Map<String, List<Stock>> name2StockMap = new HashMap<String, List<Stock>>();
		for (Stock stock : stockList) {
			String name = stock.getName();
			// 设置工厂名称
			stock.setFactoryName(null == factoryId2NameMap.get(stock
					.getFactoryId()) ? "" : factoryId2NameMap.get(stock
					.getFactoryId()));
			// 重新调整入库时间
			String inputStockTime = DateUtils.convert2String(DateUtils
					.convert2Date(stock.getCreationTime(),
							"yyyy-MM-dd HH:mm:ss"), "yyyy年MM月dd日 HH:mm:ss");
			stock.setCreationTime(inputStockTime);
			List<Stock> tempList = name2StockMap.get(name);
			if (null == tempList) {
				tempList = new ArrayList<Stock>();
				name2StockMap.put(name, tempList);
			}
			tempList.add(stock);
		}
		for (Storage storage : list) {
			String name = storage.getName();
			List<Stock> tempList = name2StockMap.get(name);
			if (null != tempList && tempList.size() > 0) {
				storage.setStockList(tempList);
			}
		}
		return list;
	}

	@Override
	public void removeStorage(String name, String year, String storeId) {
		stockDAO.deleteStorage(name, year, storeId);
		stockDAO.deleteStock(name, year, storeId);
	}

	@Override
	public int getStockSizeByFactoryId(String factoryId) {
		return stockDAO.findStocksByFactoryId(factoryId);
	}

	@Override
	public Stock getColorAndSizeByNameAndStoreId(String name, String storeId) {
		String year = ActionUtil.getLunarCalendarYear();
		return stockDAO.findStocksByStoreIdAndNameAndYear(storeId, name, year);
	}

	@Override
	public Storage getStorageByNameAndStoreId(String name, String storeId) {
		String year = ActionUtil.getLunarCalendarYear();
		return stockDAO.findStorageByNameAndStoreId(year, name, storeId);
	}

	@Override
	public void addStocks(List<Stock> stockList) {
		stockDAO.insertStocks(stockList);
	}

}
