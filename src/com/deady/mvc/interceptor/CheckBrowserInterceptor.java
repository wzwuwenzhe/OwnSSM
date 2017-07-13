package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.BrowserChecker;
import com.deady.mvc.exception.BrowserDenyException;

public class CheckBrowserInterceptor extends HandlerInterceptorAdapter {

	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		BrowserChecker interceptor = method.getAnnotation(BrowserChecker.class);
		if (interceptor == null || interceptor.checkBrowserVersion()) {
			// 客户端浏览器的版本号
			String userAgent = request.getHeader("user-agent");
			if (StringUtils.isEmpty(userAgent)) {
				throw new BrowserDenyException(request);
			}
			userAgent = userAgent.toLowerCase();
			request.setAttribute("_UA_", userAgent);
			String denys = config.getString("userAgent.deny", "").toLowerCase();
			if (StringUtils.isEmpty(denys)) {
				return true;
			}
			String[] keys = denys.split("#");
			for (String key : keys) {
				if (userAgent.contains(key)) {
					throw new BrowserDenyException(request);
				}
			}
		}
		return true;
	}
}
