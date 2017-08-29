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
import com.deady.entity.operator.Operator;
import com.deady.service.ClientService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class ClientRegisterAction {

	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/clientRegister", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object clientRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String clientId = req.getParameter("clientId");
		if (!StringUtils.isEmpty(clientId)) {
			Client client = clientService.getClientById(clientId);
			req.setAttribute("client", client);
			req.setAttribute("clientId", clientId);
		}
		return new ModelAndView("/register/client_register");
	}

	@RequestMapping(value = "/showClient", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object showClient(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator operator = OperatorSessionInfo.getOperator(req);
		List<Client> clientList = clientService.getClientListByStoreId(operator
				.getStoreId());
		req.setAttribute("clientList", clientList);
		return new ModelAndView("/client/client");
	}

	@RequestMapping(value = "/clientRegister", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object doClientRegister(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		// 目前只有前端的js验证
		// TODO 需要添加后台的信息验证
		FormResponse response = new FormResponse(req);
		Client client = new Client();
		ActionUtil.assObjByRequest(req, client);
		Operator op = OperatorSessionInfo.getOperator(req);
		client.setStoreId(op.getStoreId());
		client.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		clientService.addClient(client);
		response.setSuccess(true);
		response.setMessage("客户添加成功!");
		return response;
	}

	@RequestMapping(value = "/deleteClient", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object deleteClient(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		String clientId = req.getParameter("clientId");
		if (StringUtils.isEmpty(clientId)) {
			response.setSuccess(false);
			response.setMessage("客户编号不能为空!");
			return response;
		}
		clientService.removeClientById(clientId);
		response.setSuccess(true);
		response.setMessage("删除成功!");
		return response;
	}

	@RequestMapping(value = "/clientModify", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object clientModify(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		Client client = new Client();
		ActionUtil.assObjByRequest(req, client);
		Operator op = OperatorSessionInfo.getOperator(req);
		client.setStoreId(op.getStoreId());
		clientService.modifyClient(client);
		response.setSuccess(true);
		response.setMessage("客户修改成功!");
		return response;
	}

}
