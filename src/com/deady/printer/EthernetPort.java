package com.deady.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EthernetPort extends Port {

	// 日志
	private static final Logger logger = LoggerFactory
			.getLogger(EthernetPort.class);
	// ip地址
	InetAddress m_IPAddress;
	// socket
	Socket m_Socket = null;
	// 线程
	Thread m_Thread;
	// 输出流
	OutputStream m_OutStream;
	// 输入流
	InputStream m_InStream;
	// 异常
	Exception m_Exception = null;
	// 当前线程是否在运行
	boolean m_EthernetThreadRunning = false;
	// 连接关闭标志
	Boolean m_CloseFlag = Boolean.valueOf(false);
	// 需要发送报文标识
	Boolean m_SendFlag = Boolean.valueOf(false);
	// 发送的数据
	byte[] m_SendData;
	int m_bytesAvailable = 0;
	// 接收到的数据
	byte[] m_receiveData;
	Vector<Byte> m_receiveBuffer;

	EthernetPort(DeviceParameters parameters) {
		super(parameters);
		this.m_receiveBuffer = new Vector<Byte>(4096, 1024);
	}

	Deady.ERROR_CODE openPort() {
		logger.info("EthernetPort   ------- openPort method -------");

		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;
		this.m_Exception = null;

		this.m_Thread = new Thread(new Runnable() {
			public void run() {
				logger.info("EthernetPort   Thread started");

				EthernetPort.this.m_SendData = null;
				EthernetPort.this.m_receiveData = new byte[1024];
				EthernetPort.this.m_receiveBuffer.clear();
				EthernetPort.this.m_SendFlag = Boolean.valueOf(false);
				EthernetPort.this.m_bytesAvailable = 0;
				logger.info("EthernetPort   Buffers cleared");
				try {
					logger.info("EthernetPort   IP address: "
							+ EthernetPort.this.m_deviceParameters.IPAddress);
					logger.info("EthernetPort  Port number: "
							+ Integer
									.toString(EthernetPort.this.m_deviceParameters.PortNumber));
					EthernetPort.this.m_IPAddress = Inet4Address
							.getByName(EthernetPort.this.m_deviceParameters.IPAddress);
					SocketAddress remoteAddr = new InetSocketAddress(
							EthernetPort.this.m_IPAddress,
							EthernetPort.this.m_deviceParameters.PortNumber);
					EthernetPort.this.m_Socket = new Socket();
					EthernetPort.this.m_Socket.connect(remoteAddr, 4000);
					logger.info("EthernetPort   Socket created");
					EthernetPort.this.m_OutStream = EthernetPort.this.m_Socket
							.getOutputStream();
					EthernetPort.this.m_InStream = EthernetPort.this.m_Socket
							.getInputStream();
					logger.info("EthernetPort   Streams created");
				} catch (Exception e) {
					logger.error("EthernetPort Exception occured creating sockets and streams: "
							+ e.getMessage());
					EthernetPort.this.m_Exception = e;
				}

				if (EthernetPort.this.m_Exception == null) {
					logger.info("EthernetPort   Starting network loop");
					EthernetPort.this.m_EthernetThreadRunning = false;
					EthernetPort.this.m_CloseFlag = Boolean.valueOf(false);
					while (!EthernetPort.this.m_CloseFlag.booleanValue()) {
						try {
							if (EthernetPort.this.m_SendFlag.booleanValue()) {
								EthernetPort.this.m_OutStream
										.write(EthernetPort.this.m_SendData);
								EthernetPort.this.m_OutStream.flush();
								EthernetPort.this.m_SendFlag = Boolean
										.valueOf(false);
								logger.info("EthernetPort  Sending data: "
										+ Integer
												.toString(EthernetPort.this.m_SendData.length)
										+ " bytes");
								// logger.info("EthernetPort  Finished sending data");
							}

							EthernetPort.this.m_bytesAvailable = EthernetPort.this.m_InStream
									.available();
							if (EthernetPort.this.m_bytesAvailable > 0) {
								logger.info("EthernetPort Receiving data: "
										+ Integer
												.toString(EthernetPort.this.m_bytesAvailable)
										+ " bytes");
								int n = EthernetPort.this.m_InStream
										.read(EthernetPort.this.m_receiveData);

								for (int i = 0; i < n; i++) {
									EthernetPort.this.m_receiveBuffer.add(Byte
											.valueOf(EthernetPort.this.m_receiveData[i]));
								}

								EthernetPort.this
										.saveData(EthernetPort.this.m_receiveBuffer);

								if (EthernetPort.this.m_callbackInfo.ReceivedDataType != Deady.DATA_TYPE.NOTHING) {
									if (EthernetPort.this.m_callback != null) {
										EthernetPort.this.m_callback
												.CallbackMethod(EthernetPort.this.m_callbackInfo);
									}

									EthernetPort.this
											.afterCallbackAction(EthernetPort.this.m_callbackInfo.ReceivedDataType);
								}

							} else {
								Thread.sleep(50L);
							}

							EthernetPort.this.m_EthernetThreadRunning = true;
						} catch (Exception e) {
							logger.error("EthernetPort Exception occured in run loop: "
									+ e.getMessage());

							EthernetPort.this.m_Exception = e;
							EthernetPort.this.m_CloseFlag = Boolean
									.valueOf(true);
						}

					}

					logger.info("EthernetPort   Closing network");
					try {
						EthernetPort.this.m_OutStream = null;
						EthernetPort.this.m_Socket.close();
						EthernetPort.this.m_Socket = null;
					} catch (Exception localException1) {
					}

					EthernetPort.this.m_EthernetThreadRunning = false;
				}
			}
		});
		this.m_Thread.start();
		try {
			Thread.sleep(50L);
		} catch (Exception localException) {
		}
		do {
			try {
				Thread.sleep(50L);
			} catch (Exception localException1) {
			}
		} while ((!this.m_EthernetThreadRunning) && (this.m_Exception == null));

		if (this.m_Exception != null) {
			if ((this.m_Exception instanceof UnknownHostException)) {
				retval = Deady.ERROR_CODE.INVALID_IP_ADDRESS;
			} else if ((this.m_Exception instanceof SocketTimeoutException)) {
				retval = Deady.ERROR_CODE.TIMEOUT;
			} else {
				retval = Deady.ERROR_CODE.FAILED;
			}
		}

		return retval;
	}

	Deady.ERROR_CODE closePort() {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		Date NowDate = new Date();
		Date TimeoutDate = new Date(NowDate.getTime() + 2000L);

		while (((this.m_SendFlag.booleanValue()) || (this.m_bytesAvailable > 0))
				&& (NowDate.before(TimeoutDate))) {
			try {
				Thread.sleep(50L);
			} catch (Exception localException) {
			}
			NowDate = new Date();
		}

		if (NowDate.before(TimeoutDate)) {
			try {
				this.m_OutStream.flush();
				this.m_Socket.close();
				this.m_CloseFlag = Boolean.valueOf(true);
			} catch (IOException e) {
				retval = Deady.ERROR_CODE.FAILED;
			}
		} else {
			retval = Deady.ERROR_CODE.TIMEOUT;
		}

		return retval;
	}

	boolean isPortOpen() {
		boolean retval = true;

		if (this.m_Socket != null) {
			if ((!this.m_Socket.isConnected()) || (!this.m_Socket.isBound())
					|| (this.m_Socket.isClosed())
					|| (this.m_Socket.isInputShutdown())
					|| (this.m_Socket.isOutputShutdown())) {
				retval = false;
			}
		} else {
			retval = false;
		}

		return retval;
	}

	Deady.ERROR_CODE writeData(Vector<Byte> data) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if ((data != null) && (data.size() > 0)) {
			parseOutgoingData(data);

			if ((this.m_Socket != null) && (this.m_OutStream != null)) {//
				Date NowDate = new Date();
				Date TimeoutDate = new Date(NowDate.getTime() + 3000L);

				while ((this.m_SendFlag.booleanValue())
						&& (NowDate.before(TimeoutDate))) {
					try {
						Thread.sleep(50L);
					} catch (InterruptedException localInterruptedException) {
					}
					NowDate = new Date();
				}

				if (NowDate.before(TimeoutDate)) {
					this.m_SendData = new byte[data.size()];

					if (data.size() > 0) {
						for (int i = 0; i < data.size(); i++) {
							this.m_SendData[i] = ((Byte) data.get(i))
									.byteValue();
						}
						this.m_SendFlag = Boolean.valueOf(true);
					}
				} else {
					retval = Deady.ERROR_CODE.TIMEOUT;
				}
			} else {
				retval = Deady.ERROR_CODE.FAILED;
			}
		}

		return retval;
	}

	protected Deady.ERROR_CODE writeDataImmediately(Vector<Byte> data) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if ((data != null) && (data.size() > 0)) {
			byte[] sendData = new byte[data.size()];

			if (data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					sendData[i] = ((Byte) data.get(i)).byteValue();
				}

				try {
					this.m_OutStream.write(sendData);
					this.m_OutStream.flush();
				} catch (Exception e) {
					logger.error("EthernetPort Exception occured while sending data immediately: "
							+ e.getMessage());
					retval = Deady.ERROR_CODE.FAILED;
				}
			}
		}

		return retval;
	}
}