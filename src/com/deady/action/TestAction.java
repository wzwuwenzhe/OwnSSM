package com.deady.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;

@Controller
public class TestAction {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@DeadyAction(checkLogin = false)
	public Object testMVC(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("test");
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@DeadyAction(checkLogin = false)
	@ResponseBody
	public Object doTestMVC(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		return response;
	}

}
