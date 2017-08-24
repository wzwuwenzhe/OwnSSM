package com.deady.action.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.entity.operator.Operator;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class IndexAction {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object loginView(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator operator = OperatorSessionInfo.getOperator(req);
		req.setAttribute("userType", operator.getUserType());
		return new ModelAndView("/index/index");
	}

	@RequestMapping(value = "/changeAccount", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object changeAccount(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		// 如果选择记住就将用户名和密码存到Cookie中否则就删掉
		OperatorSessionInfo.saveCookie(req, res);
		return new ModelAndView("/login/login");
	}

	@RequestMapping(value = "/operatorInfo", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object userInfo(HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		return new ModelAndView("/operator/operatorInfo");
	}
}
