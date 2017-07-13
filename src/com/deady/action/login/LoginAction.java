package com.deady.action.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;

@Controller
public class LoginAction {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@DeadyAction(checkReferer = true, checkLogin = false)
	public Object loginView(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("/login/login");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@DeadyAction(checkReferer = true, checkLogin = false)
	public Object redirectLogin(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("redirect:/login");
	}
}
