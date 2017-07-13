package test.book;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.utils.RequestHandler;
import test.utils.TestInitUtil;

import com.deady.action.book.BookAction;

public class BookActionTest extends TestInitUtil {

	@Autowired
	private BookAction action;

	@Test
	public void testBookList() throws Exception {
		RequestHandler.buildRequest(req, session, new Object());
		action.bookList(req, res);
	}

}
