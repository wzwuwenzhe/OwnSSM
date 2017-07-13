package test.utils;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;

public class SessionHandler {

	/**
	 * 
	 * @param name
	 *            session中属性名
	 * @param object
	 *            返回的对象
	 * @param invokeTimes
	 *            调用的次数
	 * @return
	 */

	public static Object setAttribute(HttpSession session, String name,
			Object object) {
		EasyMock.expect(session.getAttribute(name)).andReturn(object)
				.anyTimes();
		return null;

	}

}
