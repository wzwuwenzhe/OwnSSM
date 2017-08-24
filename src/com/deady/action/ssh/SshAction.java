package com.deady.action.ssh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deady.annotation.DeadyAction;
import com.deady.utils.SSHHelper;
import com.deady.utils.SSHResInfo;
import com.jcraft.jsch.JSchException;

@Controller
public class SshAction {

	@RequestMapping(value = "/shutdownPC", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	public Object clientRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		// 执行关机操作
		try {
			// 使用目标服务器机上的用户名和密码登陆
			SSHHelper helper = new SSHHelper("192.168.31.213", 22, "root",
					"wenzhebaba2");
			String command = "shutdown -h now";
			try {
				SSHResInfo resInfo = helper.sendCmd(command);
				System.out.println(resInfo.toString());
				helper.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return null;
	}
}
