package com.deady.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.ClientDAO;
import com.deady.entity.client.Client;
import com.deady.utils.PageUtils;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO clientDAO;

	@Override
	public int addClient(Client c) {
		return clientDAO.insertClient(c.getId(), c.getStoreId(), c.getName(),
				c.getPhone(), c.getDeliverAddress());
	}

	@Override
	public List<Client> getClientListByStoreId(String storeId) {
		return clientDAO.findClientListByStoreId(storeId);
	}

	@Override
	public Client getClientById(String cusId) {
		return clientDAO.findClientById(cusId);
	}

	@Override
	public List<Map<String, String>> getClientId2NameMap(String storeId) {
		return clientDAO.findClientId2NameMap(storeId);
	}

	@Override
	public void removeClientById(String clientId) {
		clientDAO.deleteClientById(clientId);
	}

	@Override
	public void modifyClient(Client client) {
		clientDAO.updateClient(client);
	}

	@Override
	public List<Client> getClientsByNameAndStoreId(String cusName,
			String storeId) {
		return clientDAO.findClientsByNameAndStoreId(cusName, storeId);
	}

	@Override
	public List<Client> getClientListByStoreId4Page(String storeId,
			PageUtils page) {
		// 查询条件总数
		int total = clientDAO.countClientListByStoreId(storeId);
		page.setTotal(total);
		// 按照分页条件进行查询
		return clientDAO.findClientListByStoreId4Page(storeId,
				(page.getStart() - 1) * page.getPagesize(), page.getPagesize());
	}

	@Override
	public void updateClientAddressById(String id, String newDeliverAddress) {
		clientDAO.modifyClientAddressById(id, newDeliverAddress);
	}

}
