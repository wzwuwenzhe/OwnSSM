package test;

import org.junit.Test;

import com.deady.printer.Deady.ALIGNMENT;
import com.deady.printer.Deady.FONT;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;

public class PrinterTestUtil {

	@Test
	public void test() {
		// 设置设备参数
		// EpsonComDevice dev = new EpsonComDevice();
		// EpsonComDeviceParameters param = new EpsonComDeviceParameters();
		// param.PortType = EpsonCom.PORT_TYPE.ETHERNET;
		// param.IPAddress = "192.168.31.101";
		// param.PortNumber = 9100;
		// dev.setDeviceParameters(param);
		// dev.openDevice();
		// dev.selectReceiptPaper();
		// dev.selectAlignment(ALIGNMENT.CENTER);
		// dev.printString("一二三", FONT.FONT_A, false, false, false, false);
		// dev.closeDevice();

		Device device = new Device();
		DeviceParameters params = new DeviceParameters();
		device.setDeviceParameters(params);
		device.openDevice();
		device.selectReceiptPaper();
		// 中间是无效的
		device.selectAlignment(ALIGNMENT.CENTER);
		device.printString("一二三", FONT.FONT_A, false, false, false, false);
		device.closeDevice();
	}

}
