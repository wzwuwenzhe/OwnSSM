package com.deady.mvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deady.annotation.DeadyAction;

/**
 * @author Andre.Z 2014-10-30 下午4:48:27<br>
 * 
 */
public class EncodeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		String encode = request.getCharacterEncoding();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			request.setCharacterEncoding((interceptor == null) ? "UTF-8"
					: interceptor.requestEncoding());
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor != null) {
			response.setCharacterEncoding((interceptor == null) ? "UTF-8"
					: interceptor.responseEncoding());
		}

	}
}
