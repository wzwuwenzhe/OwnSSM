package com.deady.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeadyAction {

	String requestEncoding() default "UTF-8"; // request请求所用的编码，默认为“UTF-8”

	String responseEncoding() default "UTF-8"; // response回应输出的编码，默认为“UTF-8”

	boolean restoreWhenError() default true; // 发生错误后，是否要进行页面内容还原

	boolean checkLogin() default true; // 是否进行登录检测

	boolean checkCode() default false; // 方法是否需要经过验证码检测，默认为否

	String codeParameterName() default "vcode"; // 验证码在请求参数中的名称，默认为"vcode"

	boolean createToken() default false; // 是否创建一个token字串以便于进行请求重复性校验

	boolean checkToken() default false; // 是否检测该次请求的重复性

	boolean checkReferer() default false; // 方法是否需要经过来源检测，默认为否

	boolean acceptNullReferer() default true; // 来源是否允许为空，默认为是。直接请求时来源是为空的

	boolean checkServerDomain() default false; // 是否检测域名在允许的域中，默认为否

}
