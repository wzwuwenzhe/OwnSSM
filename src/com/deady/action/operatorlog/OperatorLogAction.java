package com.deady.action.operatorlog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.entity.operator.Operator;
import com.deady.entity.operator.UserType;
import com.deady.entity.operatorlog.OperatorLog;
import com.deady.entity.operatorlog.OperatorLogSearchEntity;
import com.deady.service.OperatorLogService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.PageUtils;

@Controller
public class OperatorLogAction {

	private static Logger log = LoggerFactory
			.getLogger(OperatorLogAction.class);
	@Autowired
	private OperatorLogService logService;

	@RequestMapping(value = "/logSearch", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object viewLogSearch(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		return new ModelAndView("/operatorLog/operatorLog");
	}

	@RequestMapping(value = "/logSearch", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	public Object doLogSearch(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		PageUtils page = new PageUtils(req);
		OperatorLogSearchEntity entity = new OperatorLogSearchEntity();
		ActionUtil.assObjByRequest(req, entity);
		List<OperatorLog> logList = logService.getOperatorLogByCondition4Page(
				entity, page);
		req.setAttribute("logList", logList);
		req.setAttribute("entity", entity);
		req.setAttribute("page", page);
		return new ModelAndView("/operatorLog/operatorLog");
	}

}
