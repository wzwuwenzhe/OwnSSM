package com.deady.action.register;

import java.util.List;
import java.util.UUID;

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
import com.deady.entity.client.Client;
import com.deady.entity.factory.Factory;
import com.deady.entity.operator.Operator;
import com.deady.service.FactoryService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class FactoryRegisterAction {

	@Autowired
	private FactoryService factoryService;

	@RequestMapping(value = "/showFactory", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object showFactory(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator operator = OperatorSessionInfo.getOperator(req);
		List<Factory> clientList = factoryService
				.getFactoryListByStoreId(operator.getStoreId());
		req.setAttribute("factoryList", clientList);
		return new ModelAndView("/factory/factory");
	}

	@RequestMapping(value = "/factoryRegister", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object clientRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String factoryId = req.getParameter("factoryId");
		if (!StringUtils.isEmpty(factoryId)) {
			Factory factory = factoryService.getFactoryById(factoryId);
			req.setAttribute("factory", factory);
			req.setAttribute("factoryId", factoryId);
		}
		return new ModelAndView("/factory/factory_register");
	}

	@RequestMapping(value = "/factoryRegister", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object doClientRegister(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		Factory factory = new Factory();
		ActionUtil.assObjByRequest(req, factory);
		Operator op = OperatorSessionInfo.getOperator(req);
		factory.setStoreId(op.getStoreId());
		factory.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		factoryService.addFactory(factory);
		response.setSuccess(true);
		response.setMessage("工厂添加成功!");
		return response;
	}

	@RequestMapping(value = "/deleteFactory", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object deleteFactory(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		String factoryId = req.getParameter("factoryId");
		if (StringUtils.isEmpty(factoryId)) {
			response.setSuccess(false);
			response.setMessage("工厂编号不能为空!");
			return response;
		}
		factoryService.removeFactoryById(factoryId);
		response.setSuccess(true);
		response.setMessage("删除成功!");
		return response;
	}

	@RequestMapping(value = "/factoryModify", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object factoryModify(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		Factory factory = new Factory();
		ActionUtil.assObjByRequest(req, factory);
		Operator op = OperatorSessionInfo.getOperator(req);
		factory.setStoreId(op.getStoreId());
		factoryService.modifyFactory(factory);
		response.setSuccess(true);
		response.setMessage("工厂信息修改成功!");
		return response;
	}

}
