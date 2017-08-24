package com.deady.dao;

import org.apache.ibatis.annotations.Param;

import com.deady.entity.operator.Operator;

public interface OperatorDAO {

	int insertOperator(@Param("id") String id,
			@Param("storeId") String storeId, @Param("name") String name,
			@Param("phone") String phone, @Param("userType") String userType,
			@Param("loginName") String loginName, @Param("pwd") String pwd);

	Operator getOperatorByLoginName(String loginName);

	int getOperatorCountByStoreId(String storeId);

	void updateOperatorPwdById(@Param("id") String id, @Param("pwd") String pwd);

}
