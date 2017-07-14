package com.deady.action.register;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		return new ModelAndView("/register/client_register");
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

}
