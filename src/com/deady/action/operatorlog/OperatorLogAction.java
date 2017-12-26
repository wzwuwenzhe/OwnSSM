package com.deady.action.operatorlog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.entity.operator.Operator;
import com.deady.entity.operator.UserType;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class OperatorLogAction {

	private static Logger log = LoggerFactory
			.getLogger(OperatorLogAction.class);

	@RequestMapping(value = "/logSearch", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object billing(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		if (!op.getUserType().equals(UserType.Admin.getValue() + "")) {
			return new ModelAndView("/index/index");
		}
		return new ModelAndView("/operatorLog/operatorLog");
	}

}
