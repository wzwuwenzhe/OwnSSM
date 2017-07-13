package com.deady.mvc.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.deady.annotation.DeadyAction;

/**
 * @author Andre.Z 2014-11-4 上午11:07:01<br>
 * 
 */
public class RestoreWhenErrorInterceptor extends HandlerInterceptorAdapter {

	private ThreadLocal<Boolean> errorOccured = new ThreadLocal<Boolean>();

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor == null || interceptor.restoreWhenError() == false) {
			return true;
		}
		this.errorOccured.set(false);
		Object obj = request.getSession().getAttribute("_errorEnv");
		List<String> excepKeys = new ArrayList<String>();
		excepKeys.add("_exception");
		excepKeys.add("_referer");
		if (obj != null) {
			this.errorOccured.set(true);
			Map<String, Object> env = (Map<String, Object>) obj;
			Set<String> keys = env.keySet();
			Set<String> queryParameterNames = request.getParameterMap()
					.keySet(); // 还原页面的时候，是直接用的页面跳转，获取到的只会是url传递的
			for (String key : keys) {
				if (excepKeys.contains(key)) {
					// 转入错误页面时，Model中已经有了这两个key，在不允许Override时，会导致后面Model赋值出错，设置Override会有副作用，故跳过
					continue;
				}
				if (queryParameterNames.contains(key)) {
					// 不占用url传递过来的键
					continue;
				}
				request.setAttribute(key, env.get(key));
			}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		DeadyAction interceptor = method.getAnnotation(DeadyAction.class);
		if (interceptor == null || interceptor.restoreWhenError() == false) {
			return;
		}
		if (this.errorOccured.get()) {
			request.getSession().removeAttribute("_errorEnv");
			this.errorOccured.remove();
		}
	}
}
