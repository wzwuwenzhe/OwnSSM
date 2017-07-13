package com.deady.mvc.exception;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.deady.common.FormResponse;

/**
 * 统一异常处理
 * 
 * @author wuwz
 * 
 */
public class ExceptionResolver extends HandlerExceptionResolverComposite {

	private static Logger logger = LoggerFactory
			.getLogger(ExceptionResolver.class);
	public static String exceptionKey = "_exception_handler";

	@SuppressWarnings("unchecked")
	private Map<String, String> request2Map(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String key = names.nextElement();
				String value = request.getParameter(key);
				if (StringUtils.isEmpty(value)) {
					continue;
				}
				map.put(key, value);
			}
		} catch (Exception e) {
			// 转换出错的话，直接忽略先
		}
		return map;
	}

	/**
	 * 统一的异常处理
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		Map<String, Object> env = new HashMap<String, Object>();
		if (!(exception instanceof DeadyException) || null == handler) {
			logger.error("程序运行错误", exception);
			exception = new Exception("出错了，请与管理员联系！");
		}
		// 浏览器限制异常
		if (exception instanceof BrowserDenyException) {
			return new ModelAndView("browserDeny");
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		// 请求是否使用@ResponseBody 注解
		ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
		if (responseBody != null) { // 不使用视图
			Class<?> returnType = method.getReturnType();
			// 判定此 returnType 对象所表示的类或接口与指定的 FormResponse 参数所表示的类或接口是否相同
			if (returnType.isAssignableFrom(FormResponse.class)) {
				FormResponse json = new FormResponse(request);
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("success", json.isSuccess());
				model.put("message", exception.getMessage());
				model.put("data", null);
				model.put("token", json.getToken());
				// 将map中的键值对 组装成json形式 以responseBody返回到客户端
				return new ModelAndView(new MappingJackson2JsonView(), model);
			} else if (returnType.isAssignableFrom(String.class)) {
				request.setAttribute(exceptionKey, exception);
				return new ModelAndView("forward:/handleStringError");
			}
		}
		// 使用视图或前面无对应处理
		request.getSession().removeAttribute("_errorEnv");
		if (exception instanceof LoginException) {
			return new ModelAndView("redirect:/login");
		}
		env.put("_exception", exception);
		if (!StringUtils.isEmpty(request.getHeader("referer"))) {
			env.put("_referer", request.getHeader("referer"));
		}
		env.putAll(this.request2Map(request));
		request.getSession().setAttribute("_errorEnv", env);
		return new ModelAndView("error", env);
	}
}
