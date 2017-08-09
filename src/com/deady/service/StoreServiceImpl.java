package com.deady.service;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.dao.StoreDAO;
import com.deady.entity.store.Store;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private StoreDAO storeDAO;
	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");

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

	@Override
	public void uploadPic4Printer(Store store) {
		String basePath = config.getString("store.img.upload.path");
		String logoImgPath = basePath + store.getLogoImg();
		String wxAddImgPath = basePath + store.getWxAddImg();
		String wxPayImgPath = basePath + store.getWxPayImg();
		String zfbPayImgPath = basePath + store.getZfbPayImg();

		Device dev = new Device();
		DeviceParameters param = new DeviceParameters();
		dev.setDeviceParameters(param);
		dev.openDevice();
		dev.savePics(logoImgPath);
		dev.closeDevice();
	}
}
