package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.utils.ActionUtil;
import com.deady.utils.TokenUtil;

/**
 * @author Andre.Z 2014-10-23 下午1:35:40<br>
 * 
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			if (interceptor.checkToken()) {
				if (!TokenUtil.isValidToken(request)) {
					ActionUtil.alert("非法或重复请求，请刷新后重试。");
				}
			}
			if (interceptor.createToken()) {
				String token = TokenUtil.newToken(request);
				request.setAttribute(FormResponse.tokenAttrKey, token);
			}
		}
		return true;
	}

}
