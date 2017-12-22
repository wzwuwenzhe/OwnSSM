package com.deady.action.ssh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.DeadyAction;
import com.deady.utils.SSHHelper;
import com.deady.utils.SSHResInfo;
import com.jcraft.jsch.JSchException;

@Controller
public class SshAction {

	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");
	private static Logger logger = LoggerFactory.getLogger(SshAction.class);

	@RequestMapping(value = "/shutdownPC", method = RequestMethod.POST)
	@DeadyAction(checkLogin = false)
	public Object clientRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		// 执行关机操作
		try {
			String pcIp = config.getString("pc.ip");
			String pcPort = config.getString("pc.port");
			String pcUser = config.getString("pc.user");
			String pcPwd = config.getString("pc.pwd");
			// 使用目标服务器机上的用户名和密码登陆
			SSHHelper helper = new SSHHelper(pcIp, Integer.parseInt(pcPort),
					pcUser, pcPwd);
			String command = "shutdown -h now";
			try {
				SSHResInfo resInfo = helper.sendCmd(command);
				logger.info("关机命令执行后:" + resInfo.toString());
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
