package com.deady.service;

import java.util.List;

import com.deady.entity.operatorlog.OperatorLog;
import com.deady.entity.operatorlog.OperatorLogSearchEntity;

public interface OperatorLogService {

	List<OperatorLog> getOperatorLogByCondition4Page(
			OperatorLogSearchEntity searchEntity);

	/**
	 * 添加系统日志
	 * 
	 * @param log
	 */
	void addLog(OperatorLog log);
}
