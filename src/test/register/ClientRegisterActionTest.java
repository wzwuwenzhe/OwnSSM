package test.register;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.utils.RequestHandler;
import test.utils.TestInitUtil;

import com.deady.action.register.ClientRegisterAction;
import com.deady.entity.client.Client;

public class ClientRegisterActionTest extends TestInitUtil {

	private ClientRegisterAction action = new ClientRegisterAction();

	@Test
	public void testClientRegister() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoClientRegister() throws Exception {
		Client c = new Client();
		RequestHandler.buildRequest(req, session, c);
		action.doClientRegister(req, res);
	}

}
