package com.deady.action.remote;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.remote.RemoteOrder;
import com.google.gson.Gson;

@Controller
public class RemoteTaskAction {

	@RequestMapping(value = "/remoteBillPrint", method = RequestMethod.POST)
	@DeadyAction(checkReferer = true)
	@ResponseBody
	public Object doRemoteBillPrint(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Gson gson = new Gson();
		RemoteOrder order = gson.fromJson(req.getParameter("order"),
				RemoteOrder.class);

		FormResponse response = new FormResponse(req);
		return response;
	}
}
