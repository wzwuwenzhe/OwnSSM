package com.deady.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.OperatorDAO;
import com.deady.entity.operator.Operator;

@Service
public class OperatorServiceImpl implements OperatorService {

	@Autowired
	private OperatorDAO operatorDAO;

	@Override
	public int insertOperator(Operator op) {
		return operatorDAO.insertOperator(op.getId(), op.getStoreId(),
				op.getName(), op.getPhone(), op.getUserType(),
				op.getLoginName(), op.getPwd());
	}

	@Override
	public Operator getOperatorByLoginName(String userName) {
		return operatorDAO.getOperatorByLoginName(userName);
	}

}
