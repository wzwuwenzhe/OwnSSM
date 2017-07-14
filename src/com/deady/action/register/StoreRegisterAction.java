package com.deady.action.register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;

@Controller
public class StoreRegisterAction {

	@RequestMapping(value = "/storeRegister", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object storeRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("/register/store_register");
	}

}
