package com.deady.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.shunpay.message.pojo.Message;
import net.shunpay.message.util.MessageContextUtil;
import net.shunpay.message.util.PagerUtil;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.common.FormResponse;
import com.deady.mvc.exception.AlertException;

/**
 * @author Andre.Z 2014-11-6 上午9:14:23<br>
 * 
 */
public class ActionUtil {

	public static final PropertiesConfiguration errorMessages = ConfigUtil
			.getProperties("error.message");
	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("sl");

	public static ModelAndView alert(String message) throws AlertException {
		throw new AlertException(message);
	}

	public static boolean isTestMode() {
		return config.getString("test.mode.enable").equals("true");
	}

	public static ModelAndView info(String message, String nextUrl) {
		Map<String, Object> env = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(message)) {
			env.put("_info", message);
		}
		if (!StringUtils.isEmpty(nextUrl)) {
			env.put("_url", nextUrl);
		}
		return new ModelAndView("info", env);
	}

	public static String getErrorMessage(String key) {
		return errorMessages.getString(key, "不存在指定键值，获取消息失败！");
	}

	public static FormResponse getPagerDatas(
			Class<? extends Message> sendClass,
			Class<? extends Message> returnClass) throws Exception {
		return getPagerDatas(sendClass, returnClass, true);
	}

	public static FormResponse getPagerDatas(
			Class<? extends Message> sendClass,
			Class<? extends Message> returnClass, boolean autoPopulate)
			throws Exception {
		FormResponse fResponse = new FormResponse(
				MessageContextUtil.getRequest());
		Message send = sendClass.newInstance();
		if (autoPopulate) {
			send.autoPopulate();
		}
		send.send();
		if (send.success()) {
			List<? extends Message> rets = send.getList(returnClass);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pInfo", PagerUtil.getPagerInfo());
			data.put("datas", rets);
			fResponse.setSuccess(true);
			fResponse.setData(data);
		} else {
			fResponse.setMessage(send.errorReason());
		}
		return fResponse;
	}

	public static FormResponse getPagerDatas(Message send,
			Class<? extends Message> returnClass) throws Exception {
		FormResponse fResponse = new FormResponse(
				MessageContextUtil.getRequest());
		send.send();
		if (send.success()) {
			List<? extends Message> rets = send.getList(returnClass);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pInfo", PagerUtil.getPagerInfo());
			data.put("datas", rets);
			fResponse.setSuccess(true);
			fResponse.setData(data);
		} else {
			fResponse.setMessage(send.errorReason());
		}
		return fResponse;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Message> List<T> getListDatas(
			FormResponse fResponse, Class<T> returnClass) {
		if (!fResponse.isSuccess()) {
			return new ArrayList<T>();
		}
		List<T> rets = (List<T>) ((Map<String, Object>) fResponse.getData())
				.get("datas");
		return rets;
	}

	public static FormResponse send(Class<? extends Message> sendClass,
			Class<? extends Message> returnClass) throws Exception {
		return send(sendClass, returnClass, true);
	}

	public static FormResponse send(Class<? extends Message> sendClass,
			Class<? extends Message> returnClass, boolean autoPopulate)
			throws Exception {
		FormResponse fResponse = new FormResponse(
				MessageContextUtil.getRequest());
		Message send = sendClass.newInstance();
		if (autoPopulate) {
			send.autoPopulate();
		}
		send.send();
		if (send.success()) {
			fResponse.setSuccess(true);
			if (returnClass != null) {
				fResponse.setData(send.get(returnClass));
			}
		} else {
			fResponse.setMessage(send.errorReason());
		}
		return fResponse;
	}

	public static FormResponse send(Message send,
			Class<? extends Message> returnClass) throws Exception {
		FormResponse fResponse = new FormResponse(
				MessageContextUtil.getRequest());
		send.send();
		if (send.success()) {
			fResponse.setSuccess(true);
			if (returnClass != null) {
				fResponse.setData(send.get(returnClass));
			}
		} else {
			fResponse.setMessage(send.errorReason());
		}
		return fResponse;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Message> T getData(FormResponse fResponse,
			Class<T> returnClass) throws Exception {
		if (!fResponse.isSuccess()) {
			return returnClass.newInstance();
		}
		return (T) fResponse.getData();
	}

}
