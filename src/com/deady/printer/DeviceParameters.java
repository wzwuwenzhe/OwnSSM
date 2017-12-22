package com.deady.printer;

import net.shunpay.util.ConfigUtil;

import org.apache.commons.configuration.PropertiesConfiguration;

public class DeviceParameters {

	public String PortName;
	public int PortNumber;
	public String IPAddress;
	public char DeviceID;
	public String DeviceName;
	private static PropertiesConfiguration conf = ConfigUtil
			.getProperties("deady");

	public DeviceParameters() {
		this.PortName = "";
		this.PortNumber = Integer.parseInt(conf.getString("printer.port"));
		this.IPAddress = conf.getString("printer.ip");
		this.DeviceID = '\000';
		this.DeviceName = "";
	}

	public DeviceParameters copy() {
		DeviceParameters dp = new DeviceParameters();
		dp.PortName = this.PortName;
		dp.PortNumber = this.PortNumber;
		dp.IPAddress = this.IPAddress;
		dp.DeviceID = this.DeviceID;
		dp.DeviceName = this.DeviceName;

		return dp;
	}
}
