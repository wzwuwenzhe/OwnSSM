package com.deady.common;

import javax.servlet.http.HttpServletRequest;

import com.deady.utils.OperatorSessionInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Andre.Z 2014-11-12 下午4:03:17<br>
 * 
 */
public class FormResponse {

	@JsonIgnore
	public static String tokenAttrKey = "_form_response_token";
	@JsonIgnore
	private HttpServletRequest request = null;
	@JsonIgnore
	public static String updateAgentBalanceAttrKey = "_form_response_updateAgentBalance";
	@JsonIgnore
	public static String agentBalanceAttrKey = "_form_response_agentBalance ";

	private boolean success = false;
	private String message = null;
	private Object data = null;
	@SuppressWarnings("unused")
	private String token = null;

	private int authType = AuthType.None.getValue();

	@SuppressWarnings("unused")
	private boolean updateAgentBalance = false;
	@SuppressWarnings("unused")
	private String agentBalance = null;

	public FormResponse(HttpServletRequest request) {
		this.request = request;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getToken() {
		return (String) this.request.getAttribute(FormResponse.tokenAttrKey);
	}

	public int getAuthType() {
		return this.authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public boolean isUpdateAgentBalance() {
		if (this.request.getAttribute(FormResponse.updateAgentBalanceAttrKey) == null) {
			return false;
		}
		return (Boolean) this.request
				.getAttribute(FormResponse.updateAgentBalanceAttrKey);
	}

	public String getAgentBalance() {
		return (String) this.request
				.getAttribute(FormResponse.agentBalanceAttrKey);
	}

}
