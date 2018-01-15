package com.deady.action.ssh;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.store.Store;
import com.deady.utils.HttpClientUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class SshAction {

	private static Logger log = LoggerFactory.getLogger(SshAction.class);

	@RequestMapping(value = "/shutdownPC", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	public Object clientRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		Store store = OperatorSessionInfo.getStore(req);
		String url = store.getRemoteHttpAddress();
		log.info("---------------调用远程关机--------------");
		String back = HttpClientUtil.sendPost(url + "/shutdownPC",
				new HashMap<String, Object>(), "UTF-8");
		if (StringUtils.isEmpty(back)) {
			log.info("back:" + back);
		}
		log.info("---------------调用结束--------------");
		return response;
	}
}
