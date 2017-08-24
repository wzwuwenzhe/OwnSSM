package com.deady.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.deady.entity.client.Client;

public interface ClientDAO {

	int insertClient(@Param("id") String id, @Param("storeId") String storeId,
			@Param("name") String name, @Param("phone") String phone);

	List<Client> findClientListByStoreId(String storeId);

	Client findClientById(String cusId);

	List<Map<String, String>> findClientId2NameMap(String storeId);

	void deleteClientById(String clientId);

	void updateClient(Client client);

}
