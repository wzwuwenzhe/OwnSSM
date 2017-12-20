package com.deady.entity.remote;

import java.io.Serializable;

import com.deady.dto.OrderDto;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;

public class RemoteOrder implements Serializable {

	private static final long serialVersionUID = 8553863865344690744L;

	private String privateKey;
	private OrderDto order;
	private Store store;
	private Client client;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
