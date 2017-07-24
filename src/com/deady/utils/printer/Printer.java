package com.deady.utils.printer;

import com.epson.EpsonCom.EpsonComDevice;
import com.epson.EpsonCom.EpsonComDeviceParameters;
import com.epson.EpsonCom.EpsonCom.FONT;
import com.epson.EpsonCom.EpsonCom.PORT_TYPE;

public class Printer extends EpsonComDevice {

	private EpsonComDevice dev = null;

	public Printer(String ip, int port) {
		// 设置设备参数
		this.dev = new EpsonComDevice();
		EpsonComDeviceParameters param = new EpsonComDeviceParameters();
		param.PortType = PORT_TYPE.ETHERNET;
		param.IPAddress = ip;
		param.PortNumber = port;
		this.dev.setDeviceParameters(param);
		this.dev.openDevice(); // 打开设备
		this.dev.selectReceiptPaper();
		this.dev.selectPageMode(); // 页面模式

	}

	public void print(String string) {
		this.dev.printString(string, FONT.FONT_A.FONT_A, false, false, false,
				false);
	}

}
