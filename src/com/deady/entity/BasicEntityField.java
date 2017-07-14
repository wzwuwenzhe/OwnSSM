package com.deady.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.deady.entity.operator.UserType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BasicEntityField {

	int length(); // 字段长度

	UserType userType() default UserType.Seller; // 操作员用户类型

	String testValue() default "";// 测试数据
}
