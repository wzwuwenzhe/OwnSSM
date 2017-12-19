package com.deady.printer;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterTools {

	private static final Logger logger = LoggerFactory
			.getLogger(PrinterTools.class);

	public static Vector<Byte> convertEscposToBinary(String escpos) {
		int value = -1;

		StringReader r = new StringReader(escpos);
		StreamTokenizer st = new StreamTokenizer(r);
		st.resetSyntax();
		st.slashSlashComments(false);
		st.slashStarComments(false);
		st.whitespaceChars(0, 32);
		st.wordChars(32, 255);
		st.quoteChar(34);// 双引号
		st.quoteChar(39);// 单引号
		st.eolIsSignificant(false);

		Vector<Byte> binaryData = new Vector<Byte>(100, 50);// 100为初始容量 50为增量容量
		try {
			while (st.nextToken() != StreamTokenizer.TT_EOF) {// 只要没有读到字符的末尾
				switch (st.ttype) {
				case StreamTokenizer.TT_WORD:// 识别到文字
					String s = st.sval;
					value = -1;

					if ((s.length() == 1) && (!Character.isDigit(s.charAt(0)))) {
						byte[] bytes = s.getBytes();
						value = bytes[0];
					} else if ((s.length() > 2) && (s.substring(0, 2) == "0x")) {
						value = Integer.parseInt(s.substring(2), 16);
					} else if (isInteger(s)) {
						value = Integer.parseInt(s);
					} else if (s.contentEquals("NUL")) {
						value = Deady.ASCII_CONTROL_CODE.NUL.getASCIIValue();
					} else if (s.contentEquals("SOH")) {
						value = Deady.ASCII_CONTROL_CODE.SOH.getASCIIValue();
					} else if (s.contentEquals("STX")) {
						value = Deady.ASCII_CONTROL_CODE.STX.getASCIIValue();
					} else if (s.contentEquals("ETX")) {
						value = Deady.ASCII_CONTROL_CODE.ETX.getASCIIValue();
					} else if (s.contentEquals("EOT")) {
						value = Deady.ASCII_CONTROL_CODE.EOT.getASCIIValue();
					} else if (s.contentEquals("ENQ")) {
						value = Deady.ASCII_CONTROL_CODE.ENQ.getASCIIValue();
					} else if (s.contentEquals("ACK")) {
						value = Deady.ASCII_CONTROL_CODE.ACK.getASCIIValue();
					} else if (s.contentEquals("BEL")) {
						value = Deady.ASCII_CONTROL_CODE.BEL.getASCIIValue();
					} else if (s.contentEquals("BS")) {
						value = Deady.ASCII_CONTROL_CODE.BS.getASCIIValue();
					} else if (s.contentEquals("HT")) {
						value = Deady.ASCII_CONTROL_CODE.HT.getASCIIValue();
					} else if (s.contentEquals("LF")) {
						value = Deady.ASCII_CONTROL_CODE.LF.getASCIIValue();
					} else if (s.contentEquals("VT")) {
						value = Deady.ASCII_CONTROL_CODE.VT.getASCIIValue();
					} else if (s.contentEquals("FF")) {
						value = Deady.ASCII_CONTROL_CODE.FF.getASCIIValue();
					} else if (s.contentEquals("CR")) {
						value = Deady.ASCII_CONTROL_CODE.CR.getASCIIValue();
					} else if (s.contentEquals("SO")) {
						value = Deady.ASCII_CONTROL_CODE.SO.getASCIIValue();
					} else if (s.contentEquals("SI")) {
						value = Deady.ASCII_CONTROL_CODE.SI.getASCIIValue();
					} else if (s.contentEquals("DLE")) {
						value = Deady.ASCII_CONTROL_CODE.DLE.getASCIIValue();
					} else if (s.contentEquals("DC1")) {
						value = Deady.ASCII_CONTROL_CODE.DC1.getASCIIValue();
					} else if (s.contentEquals("DC2")) {
						value = Deady.ASCII_CONTROL_CODE.DC2.getASCIIValue();
					} else if (s.contentEquals("DC3")) {
						value = Deady.ASCII_CONTROL_CODE.DC3.getASCIIValue();
					} else if (s.contentEquals("DC4")) {
						value = Deady.ASCII_CONTROL_CODE.DC4.getASCIIValue();
					} else if (s.contentEquals("NAK")) {
						value = Deady.ASCII_CONTROL_CODE.NAK.getASCIIValue();
					} else if (s.contentEquals("SYN")) {
						value = Deady.ASCII_CONTROL_CODE.SYN.getASCIIValue();
					} else if (s.contentEquals("ETB")) {
						value = Deady.ASCII_CONTROL_CODE.ETB.getASCIIValue();
					} else if (s.contentEquals("CAN")) {
						value = Deady.ASCII_CONTROL_CODE.CAN.getASCIIValue();
					} else if (s.contentEquals("EM")) {
						value = Deady.ASCII_CONTROL_CODE.EM.getASCIIValue();
					} else if (s.contentEquals("SUB")) {
						value = Deady.ASCII_CONTROL_CODE.SUB.getASCIIValue();
					} else if (s.contentEquals("ESC")) {
						value = Deady.ASCII_CONTROL_CODE.ESC.getASCIIValue();
					} else if (s.contentEquals("FS")) {
						value = Deady.ASCII_CONTROL_CODE.FS.getASCIIValue();
					} else if (s.contentEquals("GS")) {
						value = Deady.ASCII_CONTROL_CODE.GS.getASCIIValue();
					} else if (s.contentEquals("RS")) {
						value = Deady.ASCII_CONTROL_CODE.RS.getASCIIValue();
					} else if (s.contentEquals("US")) {
						value = Deady.ASCII_CONTROL_CODE.US.getASCIIValue();
					}

					if (value == -1) {
						continue;
					}
					Byte b = new Byte((byte) value);
					binaryData.add(b);

					break;
				case StreamTokenizer.TT_NUMBER:
					String s2 = st.sval;

					for (int i = 0; i < s2.length(); i++) {
						byte b2 = s2.getBytes()[i];
						binaryData.add(new Byte(b2));
					}
					break;
				default:
					String s3 = st.sval;

					for (int i = 0; i < s3.length(); i++) {
						byte b2 = s3.getBytes()[i];
						binaryData.add(new Byte(b2));
					}
					break;
				}
			}

		} catch (NumberFormatException localNumberFormatException) {
			logger.error(localNumberFormatException.getMessage(),
					localNumberFormatException);
		} catch (IOException localIOException) {
			logger.error(localIOException.getMessage(), localIOException);
		}

		return binaryData;
	}

	public static Vector<Byte> convertHexadecimalToBinary(
			Vector<Byte> HexadecimalData) {
		int hexLength = HexadecimalData.size();
		int index1 = 0;
		int index2 = 0;
		byte binaryValue = 0;

		Vector<Byte> binaryData = new Vector<Byte>(hexLength / 2, 1024);

		for (index2 = 1; index2 < hexLength; index2 += 2) {
			index1 = index2 - 1;
			binaryValue = (byte) (xtod(((Byte) HexadecimalData
					.elementAt(index1)).byteValue()) * 16 + xtod(((Byte) HexadecimalData
					.elementAt(index2)).byteValue()));
			binaryData.add(new Byte(binaryValue));
		}

		return binaryData;
	}

	public static byte xtod(byte c) {
		byte retval = 0;

		if ((c >= 48) && (c <= 57))
			retval = (byte) (c - 48);
		else if ((c >= 65) && (c <= 70))
			retval = (byte) (c - 65 + 10);
		else if ((c >= 97) && (c <= 102))
			retval = (byte) (c - 97 + 10);

		return retval;
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static Vector<Byte> convertStringToBinary(String command) {
		Vector<Byte> v = new Vector<Byte>();

		if (command.lastIndexOf("'LF'") != -1) {// 打印并换行
			command = command.substring(0, command.lastIndexOf(" 'LF'"));
			for (byte b : command.getBytes()) {
				v.add(new Byte(b));
			}
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.LF.getASCIIValue()));
		} else if (command.indexOf("'ESC @'") != -1) {// 打印机初始化
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.ESC.getASCIIValue()));
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.AT.getASCIIValue()));
		} else if (command.indexOf("'ESC a'") != -1) {// 选择对齐方式
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.ESC.getASCIIValue()));
			v.add(new Byte((byte) 97));
			command = command.substring(
					command.indexOf("'ESC a'") + "'ESC a '".length(),
					command.length());
			for (byte b : command.getBytes()) {
				v.add(new Byte(b));
			}
		} else if (command.indexOf("'GS V'") != -1) {
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.GS.getASCIIValue()));
			v.add(new Byte((byte) 86));
			v.add(new Byte((byte) 66));
			v.add(new Byte((byte) 1));
		} else if (command.indexOf("'FS p'") != -1) {
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.FS.getASCIIValue()));
			v.add(new Byte((byte) 112));
			v.add(new Byte((byte) 1));
			v.add(new Byte((byte) 0));
		} else if (command.indexOf("GS !") != -1) {
			v.add(new Byte((byte) Deady.ASCII_CONTROL_CODE.GS.getASCIIValue()));
			v.add(new Byte((byte) 33));
			command = command.substring(5, command.length());
			v.add(new Byte((byte) Integer.parseInt(command)));
		}
		logger.info("command:" + command);
		return v;
	}

	public static byte[] charToByte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);
		return b;
	}

	public static void main(String[] args) {
		String command = "GS ! 85";
		if (command.indexOf("GS !") != -1) {
			System.out.println(1);
		} else {
			System.out.println(0);
		}
	}

	// TODO 存储flash位图的方法
	// TODO 读取位图方法

}
