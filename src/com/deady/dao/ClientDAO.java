package com.deady.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deady.entity.client.Client;

public interface ClientDAO {

	int insertClient(@Param("id") String id, @Param("storeId") String storeId,
			@Param("name") String name, @Param("phone") String phone);

	List<Client> findClientListByStoreId(String storeId);

	Client findClientById(String cusId);

}
