package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deady.annotation.DeadyAction;
import com.deady.mvc.exception.LoginException;
import com.deady.utils.agent.AgentUtil;

/**
 * @author Andre.Z 2014-10-23 下午1:35:40<br>
 * 
 */
public class CheckAuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			if (interceptor.checkLogin()) {
				boolean isLogin = AgentUtil.isAgentLogined(request);
				if (!isLogin) {
					throw new LoginException(request);
				}
				return true;
			}
		}
		return true;
	}
}
