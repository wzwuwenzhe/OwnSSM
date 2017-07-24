package com.deady.printer;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deady.printer.Deady.FONT;

public class Device {

	private static final Logger logger = LoggerFactory.getLogger(Device.class);
	private DeviceParameters m_DeviceParameters;
	private Port m_Port;

	public Device() {
		this.m_DeviceParameters = null;
		this.m_Port = null;
		// this.m_callback = null;
	}

	public Deady.ERROR_CODE setDeviceParameters(DeviceParameters params) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;
		// 目前ip端口等信息写死 暂时不需要验证
		// retval = params.validateParameters();
		if (retval == Deady.ERROR_CODE.SUCCESS) {
			this.m_DeviceParameters = params.copy();
		}
		return retval;
	}

	public Deady.ERROR_CODE selectReceiptPaper() {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if (this.m_Port == null) {
			retval = Deady.ERROR_CODE.FAILED;
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			String command = "GS ( G 2 0 80 1";
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		return retval;
	}

	@SuppressWarnings("unchecked")
	public Deady.ERROR_CODE selectAlignment(Deady.ALIGNMENT alignment) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;
		int iAlignment = 0;

		if (this.m_Port == null) {
			retval = Deady.ERROR_CODE.FAILED;
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			switch (alignment) {
			case CENTER:
				iAlignment = 0;
				break;
			case LEFT:
				iAlignment = 1;
				break;
			case RIGHT:
				iAlignment = 2;
				break;
			default:
				retval = Deady.ERROR_CODE.INVALID_JUSTIFICATION;
			}

		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			String command = String.format("ESC a %d",
					new Object[] { Integer.valueOf(iAlignment) });
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		return retval;
	}

	public Deady.ERROR_CODE openDevice() {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if (!isDeviceOpen().booleanValue()) {
			if (this.m_DeviceParameters != null) {
				// 暂时不需要验证
				// retval = this.m_DeviceParameters.validateParameters();
				if (retval == Deady.ERROR_CODE.SUCCESS) {
					logger.info("DeadyDevice Creating Ethernet port...");
					this.m_Port = new EthernetPort(this.m_DeviceParameters);

					if ((this.m_Port != null)
							&& (retval == Deady.ERROR_CODE.SUCCESS)) {
						logger.info("DeadyDevice Port creation successful");

						// if (this.m_callback != null) {
						// retval = this.m_Port
						// .registerCallback(this.m_callback);
						// }

						if (retval == Deady.ERROR_CODE.SUCCESS) {
							retval = this.m_Port.openPort();
							if (retval == Deady.ERROR_CODE.SUCCESS) {
								if ((this.m_Port.isPortOpen())
										&& (this.m_Port.getError() == Deady.ERROR_CODE.SUCCESS)) {
									logger.info("DeadyDevice Port is open");

									resetDevice();

									activateASB(Boolean.valueOf(false),
											Boolean.valueOf(false),
											Boolean.valueOf(false),
											Boolean.valueOf(false),
											Boolean.valueOf(false),
											Boolean.valueOf(false));
								} else {
									logger.info("DeadyDevice",
											"Port is NOT open");
								}
							} else {
								logger.info(
										"DeadyDevice",
										"openPort returned: "
												+ retval.toString());
							}
						}
					} else {
						logger.info("DeadyDevice",
								"Port creation NOT successful");
						retval = Deady.ERROR_CODE.FAILED;
					}
				}
			} else {
				retval = Deady.ERROR_CODE.NO_DEVICE_PARAMETERS;
			}
		} else {
			retval = Deady.ERROR_CODE.DEVICE_ALREADY_OPEN;
		}

		return retval;
	}

	public Boolean isDeviceOpen() {
		if (this.m_Port != null) {
			return Boolean.valueOf(this.m_Port.isPortOpen());
		}

		return Boolean.valueOf(false);
	}

	public Deady.ERROR_CODE resetDevice() {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if (this.m_Port == null) {
			retval = Deady.ERROR_CODE.FAILED;
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			String command = "ESC @";
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		return retval;
	}

	public Deady.ERROR_CODE activateASB(Boolean drawer, Boolean onoffline,
			Boolean error, Boolean paper, Boolean slip, Boolean panelbutton) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		byte commandByte = 0;

		logger.info("EpsonComDevice  ------- activateASB method -------");
		logger.info("EpsonComDevice  m_Port==null? "
				+ Boolean.toString(this.m_Port == null));

		if (this.m_Port == null) {
			return Deady.ERROR_CODE.FAILED;
		}

		if (drawer.booleanValue()) {
			commandByte = (byte) (commandByte | 0x1);
		}
		if (onoffline.booleanValue()) {
			commandByte = (byte) (commandByte | 0x2);
		}
		if (error.booleanValue()) {
			commandByte = (byte) (commandByte | 0x4);
		}
		if (paper.booleanValue()) {
			commandByte = (byte) (commandByte | 0x8);
		}
		if (slip.booleanValue()) {
			commandByte = (byte) (commandByte | 0x20);
		}
		if (panelbutton.booleanValue()) {
			commandByte = (byte) (commandByte | 0x40);
		}

		String command = String.format("GS a %d",
				new Object[] { Byte.valueOf(commandByte) });
		Vector<Byte> binaryData = PrinterTools.convertEscposToBinary(command);
		logger.info("EpsonComDevice  binaryData==null? "
				+ Boolean.toString(binaryData == null));
		logger.info("EpsonComDevice  binaryData.size()="
				+ Integer.toString(binaryData.size()));

		retval = this.m_Port.writeData(binaryData);
		logger.info("EpsonComDevice  m_Port.writeData returned: "
				+ retval.toString());

		return retval;
	}

	public Deady.ERROR_CODE printString(String string, FONT fontA,
			Boolean bold, Boolean underlined, Boolean doubleHeight,
			Boolean doubleWidth) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;
		int options = 0;
		String command = "";

		if (this.m_Port == null) {
			retval = Deady.ERROR_CODE.FAILED;
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			switch (fontA) {
			case FONT_A:
				command = "ESC M 0";
				break;
			case FONT_B:
				command = "ESC M 1";
				break;
			default:
				retval = Deady.ERROR_CODE.INVALID_FONT;
			}

		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			if (bold.booleanValue()) {
				command = "ESC E 1";
			} else {
				command = "ESC E 0";
			}
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			if (underlined.booleanValue()) {
				command = "ESC - 49";
			} else {
				command = "ESC - 48";
			}
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			options = 0;

			if (doubleHeight.booleanValue()) {
				options |= 1;
			}

			if (doubleWidth.booleanValue()) {
				options |= 16;
			}

			command = String.format("GS ! %d",
					new Object[] { Integer.valueOf(options) });
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}

		if (retval == Deady.ERROR_CODE.SUCCESS) {
			command = String.format("'%s' LF", new Object[] { string });
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}
		return retval;
	}

	public Deady.ERROR_CODE closeDevice() {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if (isDeviceOpen().booleanValue()) {
			retval = this.m_Port.closePort();
			if (retval == Deady.ERROR_CODE.SUCCESS) {
				this.m_Port = null;
			} else {
				retval = Deady.ERROR_CODE.FAILED;
			}
		}

		return retval;
	}
}
