package com.deady.printer;

public class DeviceParameters {

	public String PortName;
	public int PortNumber;
	public String IPAddress;
	public char DeviceID;
	public String DeviceName;

	public DeviceParameters() {
		this.PortName = "";
		this.PortNumber = 26691;
		this.IPAddress = "centos7.deady.tech";
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
