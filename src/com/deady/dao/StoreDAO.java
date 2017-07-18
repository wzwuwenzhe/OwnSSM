package com.deady.dao;

import org.apache.ibatis.annotations.Param;

import com.deady.entity.store.Store;

public interface StoreDAO {

	void insertStore(@Param("id") String id, @Param("name") String name,
			@Param("address") String address,
			@Param("telePhone") String telePhone,
			@Param("mobilePhone") String mobilePhone,
			@Param("reminder") String reminder,
			@Param("logoImg") String logoImg,
			@Param("wxAddImg") String wxAddImg,
			@Param("wxPayImg") String wxPayImg,
			@Param("zfbPayImg") String zfbPayImg);

	Store findStoreById(String storeId);

	void updateStore(@Param("id") String id, @Param("name") String name,
			@Param("address") String address,
			@Param("telePhone") String telePhone,
			@Param("mobilePhone") String mobilePhone,
			@Param("reminder") String reminder,
			@Param("logoImg") String logoImg,
			@Param("wxAddImg") String wxAddImg,
			@Param("wxPayImg") String wxPayImg,
			@Param("zfbPayImg") String zfbPayImg, @Param("status") int status);

}
