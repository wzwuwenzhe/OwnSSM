package com.deady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.ClientDAO;
import com.deady.entity.client.Client;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO clientDAO;

	@Override
	public int addClient(Client c) {
		return clientDAO.insertClient(c.getId(), c.getStoreId(), c.getName(),
				c.getPhone());
	}

	@Override
	public List<Client> getClientListByStoreId(String storeId) {
		return clientDAO.findClientListByStoreId(storeId);
	}

	@Override
	public Client getClientById(String cusId) {
		return clientDAO.findClientById(cusId);
	}

}
