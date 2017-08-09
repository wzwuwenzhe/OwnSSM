package test;

import org.junit.Test;

import com.deady.printer.Deady.ALIGNMENT;
import com.deady.printer.Deady.FONT;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;

public class PrinterTestUtil {

	@Test
	public void test() {
		Device device = new Device();
		DeviceParameters params = new DeviceParameters();
		device.setDeviceParameters(params);
		device.openDevice();
		// device.selectChineseMode();
		// device.selectPageMode();
		// device.printString("客户联");
		// device.printOnPageMode();
		// device.printPic();
		device.printString("123321");
		device.cutPaper();
		device.closeDevice();
	}

}
