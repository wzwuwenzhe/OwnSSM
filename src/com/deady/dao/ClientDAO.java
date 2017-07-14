package com.deady.dao;

import org.apache.ibatis.annotations.Param;

public interface ClientDAO {

	int insertClient(@Param("id") String id, @Param("storeId") String storeId,
			@Param("cusName") String cusName, @Param("cusPhone") String cusPhone);

}
