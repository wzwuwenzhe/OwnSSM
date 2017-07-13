package test.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.shunpay.message.pojo.MessageContext;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:spring-dao.xml",
		"classpath:spring-service.xml", "classpath:mvc-core.xml" })
public class TestInitUtil {

	public HttpServletRequest req;
	public HttpServletResponse res;
	public HttpSession session;

	@Before
	public void init() {
		req = EasyMock.createMock(HttpServletRequest.class);
		session = EasyMock.createMock(HttpSession.class);
		res = EasyMock.createMock(HttpServletResponse.class);
	}
}
