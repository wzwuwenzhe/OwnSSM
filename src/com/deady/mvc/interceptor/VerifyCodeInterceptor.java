package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deady.annotation.DeadyAction;
import com.deady.utils.ActionUtil;
import com.deady.utils.VerifyCodeUtil;

/**
 * @author Andre.Z 2014-11-3 下午5:24:30<br>
 * 
 */
public class VerifyCodeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			if (interceptor.checkCode()) {
				boolean valid = VerifyCodeUtil.getInstance().isValid(request);
				if (!valid) {
					ActionUtil.alert("验证码不正确。");
				}
			}
		}
		return true;
	}

}
