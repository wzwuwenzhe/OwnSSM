package com.deady.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.entity.operator.Operator;
import com.deady.entity.store.Store;

public class OperatorSessionInfo {

	public static final String OPERATOR_SESSION_ID = "DEADY_OPERATOR";
	public static final String STORE_SESSION_ID = "DEADY_STORE";

	public static final PropertiesConfiguration cacheConfig = ConfigUtil
			.getProperties("memcache");
	public static final String COOKIE_USER_NAME = "DEADY_NAME";
	public static final String COOKIE_USER_PWD = "DEADY_PWD";

	public static void save(HttpServletRequest request, String key,
			Object object) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * cacheConfig
				.getInt("memcache.sessionTimeOut"));
		session.setAttribute(key, object);
	}

	public static void saveCookie(HttpServletRequest request,
			HttpServletResponse response) {
		if (null != request.getParameter("remember")
				&& request.getParameter("remember").equals("1")) {// 记住我
			Cookie nameCookie = new Cookie(COOKIE_USER_NAME,
					request.getParameter("username"));
			// 设置Cookie的有效期为3天
			nameCookie.setMaxAge(60 * 60 * 24 * cacheConfig
					.getInt("memcache.cookieTimeOut"));
			Cookie pwdCookie = new Cookie(COOKIE_USER_PWD,
					request.getParameter("password"));
			pwdCookie.setMaxAge(60 * 60 * 24 * cacheConfig
					.getInt("memcache.cookieTimeOut"));
			response.addCookie(nameCookie);
			response.addCookie(pwdCookie);
		} else {// 删除cookie
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(COOKIE_USER_NAME)
						|| cookie.getName().equals(COOKIE_USER_PWD)) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}

	}

	public static Operator getOperator(HttpServletRequest request) {
		return (Operator) get(request, OPERATOR_SESSION_ID);
	}

	public static Store getStore(HttpServletRequest request) {
		return (Store) get(request, STORE_SESSION_ID);
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
