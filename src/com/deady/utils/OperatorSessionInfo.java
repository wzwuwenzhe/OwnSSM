package com.deady.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.entity.operator.Operator;

public class OperatorSessionInfo {

	public static final String OPERATOR_SESSION_ID = "DEADY_OPERATOR";

	public static final PropertiesConfiguration cacheConfig = ConfigUtil
			.getProperties("memcache");

	public static void save(HttpServletRequest request, String key,
			Object object) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * cacheConfig
				.getInt("memcache.sessionTimeOut"));
		session.setAttribute(key, object);
	}

	public static Operator getOperator(HttpServletRequest request) {
		return (Operator) get(request, OPERATOR_SESSION_ID);
	}

	public static void remove(HttpServletRequest request, String key) {
		HttpSession session = request.getSession();
		session.removeAttribute(key);
	}

	private static Object get(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}

	public static boolean isOperatorLogined(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return session.getAttribute(OPERATOR_SESSION_ID) != null;
	}
}
