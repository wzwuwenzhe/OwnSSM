package com.deady.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;

/**
 * @author Andre.Z 2014-10-23 下午12:47:13<br>
 * 
 */
public class TokenUtil {

	private static final Logger logger = Logger.getLogger(TokenUtil.class);

	public static final String TOKEN_SESSION_KEY = "_form_tokens";

	public static final String TOKEN_NAME_FIELD = "_token";

	public static final PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");

	protected static int maxTokensInCache = config
			.getInt("token.cache.max", 10);

	private static String generateGUID() {
		return RandomStringUtils.randomAlphanumeric(32);
	}

	public static String newToken(HttpServletRequest request) {
		String token = generateGUID();
		cacheToken(request, token);
		return token;
	}

	/**
	 * 保存token
	 * 
	 * @param request
	 * @param token
	 */
	@SuppressWarnings("unchecked")
	private static synchronized void cacheToken(HttpServletRequest request,
			String token) {
		HttpSession session = request.getSession();
		List<String> cachedTokens = (List<String>) session
				.getAttribute(TOKEN_SESSION_KEY);
		if (cachedTokens == null) {
			cachedTokens = new ArrayList<String>();
		}
		cachedTokens.add(token);
		if (cachedTokens.size() > maxTokensInCache) {
			int maxRemoveIndex = cachedTokens.size() - maxTokensInCache - 1;
			// 相当于队列,把之前加进去的token删掉
			for (int i = maxRemoveIndex; i >= 0; i--) {
				cachedTokens.remove(i);
			}
		}
		session.setAttribute(TOKEN_SESSION_KEY, cachedTokens);
		request.setAttribute(TOKEN_NAME_FIELD, token);
	}

	public static String getToken(HttpServletRequest request) {
		return request.getParameter(TOKEN_NAME_FIELD);
	}

	@SuppressWarnings("unchecked")
	public static synchronized boolean isValidToken(HttpServletRequest request) {

		String token = getToken(request);

		if (token == null) {
			logger.error("token检测时，请求中未发现token字串，直接返回false");
			return false;
		}

		HttpSession session = request.getSession();

		List<String> cachedTokens = (List<String>) session
				.getAttribute(TOKEN_SESSION_KEY);
		/**
		 * 防止页面停留，应用重启后，提交报空指针异常
		 */
		if (cachedTokens == null) {
			cachedTokens = new ArrayList<String>();
			cachedTokens.add(token);
		}
		if (!cachedTokens.contains(token)) {
			logger.error("检测到重复token字串：[" + token + "]");
			return false;
		}

		cachedTokens.remove(token);
		session.setAttribute(TOKEN_SESSION_KEY, cachedTokens);

		return true;
	}

}
