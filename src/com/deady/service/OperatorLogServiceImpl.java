package com.deady.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.OperatorLogDAO;
import com.deady.entity.operatorlog.OperatorLog;
import com.deady.entity.operatorlog.OperatorLogSearchEntity;
import com.deady.utils.PageUtils;

@Service
public class OperatorLogServiceImpl implements OperatorLogService {

	@Autowired
	private OperatorLogDAO operatorLogDAO;

	@Override
	public List<OperatorLog> getOperatorLogByCondition4Page(
			OperatorLogSearchEntity searchEntity, PageUtils page) {
		searchEntity.setPagesize(0);
		List<OperatorLog> result = operatorLogDAO
				.findOperatorLogListByCondition4Page(searchEntity);
		page.setTotal(result.size());
		searchEntity.setStart((page.getStart() - 1) * page.getPagesize());
		searchEntity.setPagesize(page.getPagesize());
		List<OperatorLog> resultList = operatorLogDAO
				.findOperatorLogListByCondition4Page(searchEntity);
		return resultList;
	}

	@Override
	public void addLog(OperatorLog log) {
		operatorLogDAO.insertLog(log);
	}

}
