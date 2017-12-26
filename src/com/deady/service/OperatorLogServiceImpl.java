package com.deady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.OperatorLogDAO;
import com.deady.entity.operatorlog.OperatorLog;
import com.deady.entity.operatorlog.OperatorLogSearchEntity;

@Service
public class OperatorLogServiceImpl implements OperatorLogService {

	@Autowired
	private OperatorLogDAO operatorLogDAO;

	@Override
	public List<OperatorLog> getOperatorLogByCondition4Page(
			OperatorLogSearchEntity searchEntity) {
		return operatorLogDAO.findOperatorLogListByCondition4Page(searchEntity);
	}

	@Override
	public void addLog(OperatorLog log) {
		operatorLogDAO.insertLog(log);
	}

}
