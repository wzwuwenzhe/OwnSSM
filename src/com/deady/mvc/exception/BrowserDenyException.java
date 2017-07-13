package com.deady.mvc.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class BrowserDenyException extends DeadyException {

	private static final Logger logger = LoggerFactory
			.getLogger(BrowserDenyException.class);

	public BrowserDenyException(HttpServletRequest request) {
		super("不支持的浏览器");
		logger.error("检测到不允许的浏览器，将禁止访问，uri=[" + request.getRequestURL()
				+ "] , queryString=[" + request.getQueryString() + "]");
	}

	@Override
	public String getCode() {
		return ExceptionCode.BrowserDeny.getCode();
	}

}
