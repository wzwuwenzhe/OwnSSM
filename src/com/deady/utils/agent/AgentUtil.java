package com.deady.utils.agent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.shunpay.message.util.MD5;
import net.shunpay.util.MemCached;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.common.FormResponse;

/**
 * @author Andre.Z 2014-10-31 下午5:06:57<br>
 * 
 */
public class AgentUtil {

	private static final String KEY_MEM_CACHED = "SYS_MEM_KEY_SL2016";
	private static final String KEY_MEM_AGENT_SESSION_INFO = "SL2016_AgentSessionInfo";

	private static final String KEY_AGT_INFO = "agtInfo";

	public static final PropertiesConfiguration cacheConfig = ConfigUtil
			.getProperties("memcache");

	public static void save(HttpServletRequest request, String key,
			Object object) {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * cacheConfig
				.getInt("memcache.sessionTimeOut"));
		session.setAttribute(key, object);
	}

	public static void remove(HttpServletRequest request, String key) {
		HttpSession session = request.getSession();
		session.removeAttribute(key);
	}

	private static Object get(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}

	/**
	 * 
	 * @param request
	 * @param message
	 *            要保存的用户信息实体类
	 * @return
	 * @throws Exception
	 */
	// public static boolean saveAgentInfo(HttpServletRequest
	// request,RETURN211010 message)
	// throws Exception {
	// AgentSessionInfo asi = new AgentSessionInfo();
	// BeanUtils.populate(asi, BeanUtils.describe(message));
	// asi.setOrgcode(message.getOrgCode());
	// if (!StringUtils.isEmpty(message.getOrgCode())) {
	// asi.setProvinceOrgcode(message.getOrgCode().substring(0, 4)
	// + "000000");
	// } else {
	// asi.setProvinceOrgcode("0000000000");
	// }
	// return saveAgentInfo(request, asi);
	// }

	private static boolean saveAgentInfo(HttpServletRequest request,
			AgentSessionInfo asi) throws Exception {
		MD5 md5 = new MD5();
		MemCached mem = MemCached.getInstance();
		String memKey = md5.getkeyBeanofStr(asi.getAgentCode());
		boolean flag = mem.set(memKey + KEY_MEM_AGENT_SESSION_INFO, asi);
		if (!flag) {
			throw new Exception("Memcached通讯出错");
		}
		if (flag) {
			save(request, KEY_MEM_CACHED, memKey);
			save(request, KEY_AGT_INFO, asi);
		}
		remove(request, "_RETURN211010");
		return flag;
	}

	public static void updateAgentBalance(HttpServletRequest request,
			String balance) throws Exception {
		AgentSessionInfo asi = getAgentInfo(request);
		asi.setAgentBalance(balance);
		saveAgentInfo(request, asi);
		request.setAttribute(FormResponse.updateAgentBalanceAttrKey, true);
		request.setAttribute(FormResponse.agentBalanceAttrKey, balance);
	}

	public static void removeAgentInfo(HttpServletRequest request) {
		remove(request, KEY_MEM_CACHED);
		remove(request, KEY_AGT_INFO);
	}

	public static boolean isAgentLogined(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return session.getAttribute(KEY_AGT_INFO) != null;
	}

	/**
	 * 
	 * @param request
	 * @return 需要返回的用户信息的实体
	 */
	public static AgentSessionInfo getAgentInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (get(request, KEY_MEM_CACHED) == null) {
			return null;
		}
		String memKey = String.valueOf(session.getAttribute(KEY_MEM_CACHED));
		AgentSessionInfo agent = (AgentSessionInfo) MemCached.getInstance()
				.get(memKey + KEY_MEM_AGENT_SESSION_INFO);
		if (agent == null) {
			// memKey存在，说明session未失效，MC中数据失效时，从session中取出数据，重置MC数据
			agent = (AgentSessionInfo) session.getAttribute(KEY_AGT_INFO);
			try {
				saveAgentInfo(request, agent);
			} catch (Exception e) {
			}
		}
		return agent;
	}

	public static void updateLockStatus(HttpServletRequest request,
			boolean isLock) throws Exception {
		AgentSessionInfo asi = getAgentInfo(request);
		asi.setLockScreen(isLock);
		saveAgentInfo(request, asi);
	}

}
