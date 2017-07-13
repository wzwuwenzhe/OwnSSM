package test.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;

public class ResponseHandler {

	public static Object setOutputStream(HttpServletResponse res,
			ServletOutputStream obj) throws IOException {
		EasyMock.expect(res.getOutputStream()).andReturn(obj).anyTimes();
		return null;
	}

}
