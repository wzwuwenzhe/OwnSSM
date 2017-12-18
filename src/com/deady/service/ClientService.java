package com.deady.service;

import java.util.List;
import java.util.Map;

import com.deady.entity.client.Client;
import com.deady.utils.PageUtils;

public interface ClientService {

	int addClient(Client c);

	List<Client> getClientListByStoreId(String storeId);

	Client getClientById(String cusId);

	List<Map<String, String>> getClientId2NameMap(String storeId);

	void removeClientById(String clientId);

	void modifyClient(Client client);

	List<Client> getClientsByNameAndStoreId(String cusName, String storeId);

	List<Client> getClientListByStoreId4Page(String storeId, PageUtils page);

	/**
	 * 根据顾客id 更新顾客送货地址
	 * 
	 * @param id
	 * @param newDeliverAddress
	 */
	void updateClientAddressById(String id, String newDeliverAddress);
}
