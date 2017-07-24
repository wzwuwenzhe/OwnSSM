package com.deady.utils.printer;

public class PrinterUtil {

	public PrinterUtil() {

	}

	public static Printer Printer(String printerIp, int port) {
		Printer p = new Printer(printerIp, port);

		return p;
	}

}
