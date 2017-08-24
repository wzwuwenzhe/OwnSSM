package com.deady.printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.stream.FileImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Device {

	private static final Logger logger = LoggerFactory.getLogger(Device.class);
	private DeviceParameters m_DeviceParameters;
	private Port m_Port;

	public Device() {
		this.m_DeviceParameters = null;
		this.m_Port = null;
		// this.m_callback = null;
	}

	/**
	 * 选择对齐方式
	 * 
	 * @param AlignType
	 *            0:左对齐,1:居中,2:右对齐
	 * @return
	 */
	public Deady.ERROR_CODE selectAlignType(int AlignType) {
		return excuseCommand("'ESC a' " + AlignType);
	}

	public void setDeviceParameters(DeviceParameters params) {
		this.m_DeviceParameters = params.copy();
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

									// 不知道这个方法的用处是什么
									// activateASB(Boolean.valueOf(false),
									// Boolean.valueOf(false),
									// Boolean.valueOf(false),
									// Boolean.valueOf(false),
									// Boolean.valueOf(false),
									// Boolean.valueOf(false));
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
		return excuseCommand("'ESC @'");
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

	/**
	 * 页模式下打印
	 */
	public Deady.ERROR_CODE printOnPageMode() {
		return excuseCommand("ESC FF");
	}

	/**
	 * 选择页打印模式
	 * 
	 * @return
	 */
	public Deady.ERROR_CODE selectPageMode() {
		return excuseCommand("ESC L");
	}

	/**
	 * 
	 * @param fontType
	 *            字体类型 0:标准ASCII字体 1:压缩ASCII字体
	 * @param bold
	 *            是否加粗 false:不加粗 true:加粗
	 * @param underline
	 *            下划线 0:不添加下划线 1:添加一点宽度下划线 2:添加两点宽度下划线
	 * @param fondSize
	 *            字体大小 数字几就表示几倍大小 最大8倍
	 * 
	 * @return
	 */
	public Deady.ERROR_CODE selectFontBoldAndFontSize(int fontType,
			boolean bold, int underline, int fondSize) {
		Deady.ERROR_CODE retval = excuseCommand("ESC M " + fontType);
		if (retval == Deady.ERROR_CODE.SUCCESS) {
			retval = excuseCommand("ESC E " + ((bold == true) ? "1" : "0"));
		}
		if (retval == Deady.ERROR_CODE.SUCCESS) {
			retval = excuseCommand("ESC - " + underline);
		}
		if (retval == Deady.ERROR_CODE.SUCCESS) {
			switch (fondSize) {// 放大的倍数
			case 2:
				retval = selectFontSize(1);
				break;
			case 3:
				retval = selectFontSize(2);
				break;
			case 4:
				retval = selectFontSize(3);
				break;
			case 5:
				retval = selectFontSize(4);
				break;
			case 6:
				retval = selectFontSize(5);
				break;
			case 7:
				retval = selectFontSize(6);
				break;
			case 8:
				retval = selectFontSize(7);
				break;
			default:// 默认1倍
				break;
			}
		}
		return retval;
	}

	private Deady.ERROR_CODE selectFontSize(int i) {
		int options = 0;
		options |= (1 * i);
		options |= (16 * i);
		String command = String.format("GS ! %d",
				new Object[] { Integer.valueOf(options) });
		return excuseCommand(command);
	}

	public Deady.ERROR_CODE printString(String string) {
		String command = String.format("%s 'LF'", new Object[] { string });
		return excuseCommand(command);
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

	/**
	 * 打印并向前走动line 行
	 * 
	 * @param line
	 */
	public Deady.ERROR_CODE printAndWalk(int line) {
		return excuseCommand("ESC d " + line);
	}

	public Deady.ERROR_CODE excuseCommand(String command) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;
		if (this.m_Port == null) {
			retval = Deady.ERROR_CODE.FAILED;
		}
		if (retval == Deady.ERROR_CODE.SUCCESS) {
			Vector<Byte> binaryData = PrinterTools
					.convertStringToBinary(command);
			retval = this.m_Port.writeData(binaryData);
		}
		// logger.info("command:" + command);
		return retval;
	}

	public Deady.ERROR_CODE selectChineseMode() {
		return excuseCommand("FS &");
	}

	/**
	 * 
	 * @param cutType
	 * @return
	 */
	public Deady.ERROR_CODE cutPaper() {
		return excuseCommand("'GS V'66 1");
	}

	/**
	 * 
	 * @param logoImgPath
	 * @return
	 */
	public Deady.ERROR_CODE savePics(String... picPathes) {
		Vector<Byte> command = new Vector<Byte>(1024, 1024);
		for (int i = 0; i < picPathes.length; i++) {
			command.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.FS
					.getASCIIValue()));
			command.add(new Byte((byte) 113));// q
			command.add(new Byte((byte) 1));// 1
			command.add(new Byte((byte) 9));
			command.add(new Byte((byte) 0));
			command.add(new Byte((byte) 9));
			command.add(new Byte((byte) 0));
			// byte[] imgBytes = image2byte(picPathes[i]);
			for (i = 0; i < 5; i++) {
				for (int n = 0; n < 10; n++) {
					command.add(new Byte((byte) 1));
				}
				for (int n = 0; n < 10; n++) {
					command.add(new Byte((byte) 0));
				}
			}
			// for (byte b : imgBytes) {
			// command.add(b);
			// }
		}
		this.m_Port.writeData(command);
		return null;
	}

	/**
	 * 将图片转换成byte 返回
	 * 
	 * @param path
	 * @return
	 */
	private byte[] image2byte(String path) {
		byte[] data = null;
		FileImageInputStream input = null;
		try {
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

	public Deady.ERROR_CODE printPic() {
		// 先试着打一张看看
		return excuseCommand("'FS p'");
	}
}
