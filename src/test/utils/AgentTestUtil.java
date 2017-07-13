package test.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.shunpay.message.util.MD5;
import net.shunpay.util.MemCached;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;

/**
 * @author Andre.Z 2014-10-31 下午5:06:57<br>
 * 
 */
public class AgentTestUtil {

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
		SessionHandler.setAttribute(session, key, object);
	}

	public static void remove(HttpServletRequest request, String key) {
		HttpSession session = request.getSession();
		session.removeAttribute(key);
	}

	// public static boolean saveAgentInfo(HttpServletRequest request,
	// RETURN211010 message) throws Exception {
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

	// private static boolean saveAgentInfo(HttpServletRequest request,
	// AgentSessionInfo asi) throws Exception {
	// MD5 md5 = new MD5();
	// MemCached mem = MemCached.getInstance();
	// String memKey = md5.getkeyBeanofStr(asi.getAgentCode());
	// boolean flag = mem.set(memKey + KEY_MEM_AGENT_SESSION_INFO, asi);
	// if (!flag) {
	// throw new Exception("Memcached通讯出错");
	// }
	// if (flag) {
	// save(request, KEY_MEM_CACHED, memKey);
	// save(request, KEY_AGT_INFO, asi);
	// }
	// remove(request, "_RETURN211010");
	// return flag;
	// }

}
