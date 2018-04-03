package com.deady.api.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.ApiResponse;
import com.deady.common.FormResponse;
import com.deady.entity.operator.Operator;
import com.deady.entity.store.Store;
import com.deady.service.OperatorService;
import com.deady.service.StoreService;
import com.deady.utils.MD5Utils;
import com.deady.utils.OperatorSessionInfo;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/api")
public class ApiLoginAction {

	private static Logger logger = LoggerFactory
			.getLogger(ApiLoginAction.class);

	@Autowired
	private OperatorService operatorService;
	@Autowired
	private StoreService storeService;

	/**
	 * 获取当前用户的登录状态
	 */
	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	@ResponseBody
	public Object loginView(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Cookie[] cookies = req.getCookies();
		ApiResponse response = new ApiResponse();
		if (null == cookies) {
			response.setSuccess(false);
			return response;
		}
		boolean flag = false;
		JsonObject cookieObj = new JsonObject();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(OperatorSessionInfo.COOKIE_USER_NAME)) {
				cookieObj.addProperty("username", cookie.getValue());
				continue;
			}
			if (cookie.getName().equals(OperatorSessionInfo.COOKIE_USER_PWD)) {
				cookieObj.addProperty("password", cookie.getValue());
				flag = true;
				continue;
			}
		}
		if (flag) {
			cookieObj.addProperty("remember", 1);
			response.setData(cookieObj.toString());
		}
		boolean isLogin = OperatorSessionInfo.isOperatorLogined(req);
		if (isLogin) {
			response.setSuccess(true);
			return response;
		}
		response.setSuccess(false);
		return response;
	}

	/**
	 * 登录请求
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object login(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String userName = req.getParameter("username");
		String pwd = req.getParameter("password");
		ApiResponse response = new ApiResponse();
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
			response.setSuccess(false);
			response.setMessage("用户名密码不能为空");
			return response;
		}
		Operator op = operatorService.getOperatorByLoginName(userName);
		if (StringUtils.isEmpty(op) || !op.getPwd().equals(pwd)) {
			response.setSuccess(false);
			response.setMessage("用户名或密码错误");
			return response;
		}
		Store store = storeService.getStoreById(op.getStoreId());
		// 把用户信息存到Session中
		OperatorSessionInfo.save(req, OperatorSessionInfo.OPERATOR_SESSION_ID,
				op);
		OperatorSessionInfo.save(req, OperatorSessionInfo.STORE_SESSION_ID,
				store);
		// 如果选择记住就将用户名和密码存到Cookie中否则就删掉
		OperatorSessionInfo.saveCookie(req, res);
		response.setSuccess(true);
		return response;
	}

	/**
	 * 退出登录
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public Object logout(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		ApiResponse response = new ApiResponse();
		OperatorSessionInfo.logout(req);
		response.setSuccess(true);
		return response;
	}

	@RequestMapping(value = "/changePWD", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true, checkToken = true, createToken = true)
	@ResponseBody
	public Object doChangePWD(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String oldPwd = req.getParameter("oldPwd");
		String newpassword = req.getParameter("newpassword");
		String passwordConfirm = req.getParameter("passwordConfirm");
		Operator operator = OperatorSessionInfo.getOperator(req);
		FormResponse fResponse = new FormResponse(req);
		if (null != operator && !StringUtils.isEmpty(oldPwd)) {
			if (!MD5Utils.getPwd(oldPwd).equals(operator.getPwd())) {
				fResponse.setSuccess(false);
				fResponse.setMessage("旧密码错误!");
				return fResponse;
			}
		}
		if (!MD5Utils.getPwd(newpassword).equals(
				MD5Utils.getPwd(passwordConfirm))) {
			fResponse.setSuccess(false);
			fResponse.setMessage("两次新密码输入不相同!");
			return fResponse;
		}
		operatorService.modifyPwdById(operator.getId(),
				MD5Utils.getPwd(newpassword));
		fResponse.setSuccess(true);
		fResponse.setMessage("密码修改成功!");
		return fResponse;
	}
}
