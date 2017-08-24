package com.deady.service;

import com.deady.entity.operator.Operator;

public interface OperatorService {

	/**
	 * 插入一条操作员数据
	 * 
	 * @param op
	 * @return
	 */
	int insertOperator(Operator op);

	/**
	 * 根据登录名 查找操作员信息
	 * 
	 * @param userName
	 * @return
	 */
	Operator getOperatorByLoginName(String userName);

	/**
	 * 根据店铺ID查找店铺中操作员的数量
	 * 
	 * @param storeId
	 * @return
	 */
	int getOperatorCountByStoreId(String storeId);

	void modifyPwdById(String id, String pwd);
}
