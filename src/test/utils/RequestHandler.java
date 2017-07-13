package test.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.shunpay.message.annotation.MessageField;
import net.shunpay.message.util.MessageContextUtil;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.springframework.util.StringUtils;

public class RequestHandler {

	private static final Logger logger = Logger.getLogger(RequestHandler.class);

	/**
	 * 
	 * @param name
	 *            request中的参数名
	 * @param object
	 *            返回的对象
	 * @param invokeTimes
	 *            调用次数
	 * @return
	 */
	public static Object setRequestParam(HttpServletRequest req, Object name,
			Object object) {
		if (name instanceof String) {
			return EasyMock.expect(req.getParameter((String) name))
					.andReturn(null == object ? null : (String) object)
					.anyTimes();
		}
		return null;
	}

	public static Object getSession(HttpServletRequest req, HttpSession session) {
		EasyMock.expect(req.getSession()).andReturn(session).anyTimes();
		return null;
	}

	public static <T> void buildRequest(HttpServletRequest req,
			HttpSession session, T... obj) {
		try {
			Set<String> allFieldNames = new HashSet<String>();
			// 考虑到可能传多个对象进来,所以将参数obj改为可变
			Set<Field> fieldSet = new HashSet<Field>();
			for (T o : obj) {
				Field[] tempF = o.getClass().getDeclaredFields();
				for (int i = 0; i < tempF.length; i++) {
					// 属性名
					if (null == tempF[i].getAnnotation(MessageField.class)) {
						continue;
					}
					String basicFieldName = tempF[i].getName();
					tempF[i].setAccessible(true);
					String testValue = (String) tempF[i].get(o);
					if (StringUtils.isEmpty(testValue)) {
						testValue = tempF[i].getAnnotation(MessageField.class)
								.testValue();
					}

					if (allFieldNames.contains(basicFieldName)) {
						// 为了不重复设值 报错
						continue;
					}
					setRequestParam(req, basicFieldName, testValue);
					allFieldNames.add(basicFieldName);
				}
				fieldSet.addAll(Arrays.asList(tempF));
			}

			RequestHandler.getSession(req, session);
			EasyMock.replay(req);
			// initSession(req);
			EasyMock.replay(req.getSession());
			MessageContextUtil.setRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 初始化测试登录session
	 * 
	 * @param req
	 * @param session
	 * @throws Exception
	 */
	// private static void initSession(HttpServletRequest request)
	// throws Exception {
	// RETURN211010 ret = new RETURN211010();
	// Field[] f = ret.getClass().getDeclaredFields();
	// for (int i = 0; i < f.length; i++) {
	// // 属性名
	// String basicFieldName = f[i].getName();
	// String testValue = f[i].getAnnotation(MessageField.class)
	// .testValue();
	//
	// basicFieldName = basicFieldName.substring(0, 1).toUpperCase()
	// + basicFieldName.substring(1);
	// Method writeMethod = getWriteMethod(ret.getClass(), "set"
	// + basicFieldName);
	// if (null != writeMethod) {
	// writeMethod.invoke(ret, testValue);
	// }
	// }
	// SessionHandler.setAttribute(request.getSession(),
	// "agentExtendInfoCompleted", ret.getServerPort());
	// SessionHandler.setAttribute(request.getSession(), "_RETURN211010", ret);
	// AgentTestUtil.saveAgentInfo(request, ret);
	// // session.setAttribute("agentExtendInfoCompleted",
	// // ret.getServerPort());
	// // session.setAttribute("_RETURN211010", ret);
	// // AgentTestUtil.saveAgentInfo(request, ret);
	// }

	/**
	 * 获取类的public方法
	 */
	public static Method getWriteMethod(Class<?> clazz, String methodName) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, String.class);
		} catch (Exception e) {
			logger.error(clazz.toString() + ":无定义方法 " + methodName
					+ "(String args)");
			return null;
		}
		return method;
	}
}
