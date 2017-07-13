package com.deady.mvc.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andre.Z 2014-11-3 下午3:57:33<br>
 * 
 */
@SuppressWarnings("serial")
public class LoginException extends DeadyException {

	private static final Logger logger = LoggerFactory
			.getLogger(LoginException.class);

	public LoginException(HttpServletRequest request) {
		super("登录验证失败。");
		logger.error("登录验证失败, uri=[{}], queryString=[{}]",
				request.getRequestURI(), request.getQueryString());
	}

	@Override
	public String getCode() {
		return ExceptionCode.NotLogin.getCode();
	}

}
