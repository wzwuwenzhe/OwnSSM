package test.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.shunpay.message.util.MD5;
import net.shunpay.util.MemCached;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.entity.operator.Operator;
import com.deady.utils.agent.AgentSessionInfo;

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

}
