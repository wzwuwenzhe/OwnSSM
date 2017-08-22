package com.deady.service;

import java.util.List;
import java.util.Map;

import com.deady.entity.client.Client;

public interface ClientService {

	int addClient(Client c);

	List<Client> getClientListByStoreId(String storeId);

	Client getClientById(String cusId);

	List<Map<String, String>> getClientId2NameMap(String storeId);
}
