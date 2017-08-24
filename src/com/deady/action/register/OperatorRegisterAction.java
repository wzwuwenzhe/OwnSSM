package com.deady.action.register;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class OperatorRegisterAction {

	@Autowired
	private OperatorService operatorService;

	@RequestMapping(value = "/operatorRegister", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object registerView(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		req.setAttribute("userType", op.getUserType());
		req.setAttribute("storeId", op.getStoreId());
		return new ModelAndView("/register/operator_register");
	}

	@RequestMapping(value = "/operatorRegister", method = RequestMethod.POST)
	@DeadyAction(checkToken = true, createToken = true)
	@ResponseBody
	public Object registerOperator(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		Operator op = new Operator();
		ActionUtil.assObjByRequest(req, op);
		if (isOpExists(op)) {
			response.setSuccess(false);
			response.setMessage("用户名重复!");
			return response;
		}
		// 店铺中已经注册的操作员数量
		int operatorCount = operatorService.getOperatorCountByStoreId(op
				.getStoreId());
		String count = "";
		if (operatorCount < 10) {
			count += "0" + operatorCount;
		} else {
			count += operatorCount;
		}
		op.setId(op.getStoreId() + count);
		operatorService.insertOperator(op);
		response.setSuccess(true);
		response.setMessage("注册成功!");
		return response;
	}

	private boolean isOpExists(Operator op) {
		Operator temp = operatorService.getOperatorByLoginName(op
				.getLoginName());
		if (StringUtils.isEmpty(temp)) {
			return false;// 不存在
		}
		return true;// 已存在
	}

}
