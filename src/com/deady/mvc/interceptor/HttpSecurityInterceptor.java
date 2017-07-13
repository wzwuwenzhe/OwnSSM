package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.DeadyAction;
import com.deady.utils.ActionUtil;

/**
 * @author wuwz
 * 
 */
public class HttpSecurityInterceptor extends HandlerInterceptorAdapter {

	public static final PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");

	private static final Logger logger = LoggerFactory
			.getLogger(HttpSecurityInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			// 检测请求来源
			if (interceptor.checkReferer()) {
				String referer = request.getHeader("Referer");
				if (referer == null && !interceptor.acceptNullReferer()) {
					ActionUtil.alert("非法请求！");
					return false;
				}
				if (!StringUtils.isEmpty(referer)) {
					String refererServerName = referer.replaceFirst("http://",
							"").split("/")[0];
					String[] temp = refererServerName.split("\\.");
					String domain = temp[temp.length - 2] + "."
							+ temp[temp.length - 1];
					String[] validDomains = config.getString(
							"security.referer", "").split(",");
					boolean valid = false;
					for (String d : validDomains) {
						if (d.equals(domain)) {
							valid = true;
							break;
						}
					}
					if (!valid) {
						ActionUtil.alert("非法请求！");
						logger.error("非法的来源：" + referer);
						return false;
					}
				}
			}
			if (interceptor.checkServerDomain()) {
				String serverName = request.getServerName();
				String[] temp = serverName.split("\\.");
				String domain = temp[temp.length - 2] + "."
						+ temp[temp.length - 1];
				String[] validDomains = config.getString("security.domain", "")
						.split(",");
				boolean valid = false;
				for (String d : validDomains) {
					if (d.equals(domain)) {
						valid = true;
						break;
					}
				}
				if (!valid) {
					ActionUtil.alert("主机名非法！");
					logger.error("非法的主机名：" + serverName);
					return false;
				}
			}
		}
		return true;
	}
}
