package com.deady.dao;

import java.util.List;

import com.deady.entity.operatorlog.OperatorLog;
import com.deady.entity.operatorlog.OperatorLogSearchEntity;

public interface OperatorLogDAO {

	/**
	 * 根据条件查询
	 * 
	 * @param searchEntity
	 * @return
	 */
	List<OperatorLog> findOperatorLogListByCondition4Page(
			OperatorLogSearchEntity searchEntity);

	void insertLog(OperatorLog log);
}
