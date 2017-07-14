package com.deady.action.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.operator.Operator;
import com.deady.service.OperatorService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class LoginAction {

	@Autowired
	private OperatorService operatorService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@DeadyAction(checkReferer = true, checkLogin = false, createToken = true)
	public Object loginView(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("/login/login");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@DeadyAction(checkReferer = true, checkLogin = false)
	public Object redirectLogin(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = (Operator) OperatorSessionInfo.getOperator(req);
		if (!StringUtils.isEmpty(op)) {
			return new ModelAndView("redirect:/index");
		}
		return new ModelAndView("redirect:/login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@DeadyAction(checkLogin = false, checkToken = true, createToken = true)
	@ResponseBody
	public Object login(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String userName = req.getParameter("username");
		String pwd = req.getParameter("password");
		FormResponse response = new FormResponse(req);
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
		// 把用户信息存到Session中
		OperatorSessionInfo.save(req, OperatorSessionInfo.OPERATOR_SESSION_ID,
				op);
		response.setSuccess(true);
		return response;
	}
}
