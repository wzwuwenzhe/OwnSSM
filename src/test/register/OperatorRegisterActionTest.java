package test.register;

import org.junit.Test;

import test.utils.RequestHandler;
import test.utils.TestInitUtil;

import com.deady.action.register.OperatorRegisterAction;
import com.deady.entity.operator.Operator;

public class OperatorRegisterActionTest extends TestInitUtil {

	private OperatorRegisterAction action = new OperatorRegisterAction();

	@Test
	public void testRegisterOperator() throws Exception {
		Operator o = new Operator();
		RequestHandler.buildRequest(req, session, o);
		action.registerOperator(req, res);

	}

}
