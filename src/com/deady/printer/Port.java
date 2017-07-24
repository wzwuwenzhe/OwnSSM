package com.deady.printer;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Port {

	private static final Logger logger = LoggerFactory.getLogger(Port.class);
	protected DeviceParameters m_deviceParameters;
	private Deady.DATA_TYPE m_expectedReceiveType;
	protected DeadyCallbackInfo m_callbackInfo = new DeadyCallbackInfo();
	protected CallbackInterface m_callback = null;

	private Vector<Byte> m_receiveBuffer = new Vector<Byte>(1024, 1024);
	private Vector<Byte> m_ASB = new Vector<Byte>(10);
	private byte m_RealtimeStatus;
	private Vector<Byte> m_MICRString = new Vector<Byte>(100, 100);
	private Vector<Byte> m_imageBuffer = new Vector<Byte>(1000000, 1000000);
	private Boolean m_RealtimeStatusReady;
	private Boolean m_MICRDataReady;
	private Boolean m_ImageDataReady;
	private Vector<Byte> m_fileInfoBlock = new Vector<Byte>(1024, 1024);
	private Vector<Byte> m_sizeInfoBlock = new Vector<Byte>(1024, 1024);
	private Vector<Byte> m_imageDataBlock = new Vector<Byte>(1000000, 1000000);
	private Deady.RECEIVESTATE m_receiveState;
	private Deady.RECEIVESUBSTATE m_receiveSubState;
	private long m_receiveCounter;
	private int m_dataBlockSize;
	private byte m_headerByte;
	private byte m_identifier;
	private byte m_identificationStatus;
	private ReentrantLock m_receiveBufferLock = new ReentrantLock();
	private ReentrantLock m_ASBLock = new ReentrantLock();
	private ReentrantLock m_MICRStringLock = new ReentrantLock();
	private ReentrantLock m_imageBufferLock = new ReentrantLock();

	protected Deady.ERROR_CODE m_Error = Deady.ERROR_CODE.SUCCESS;

	abstract Deady.ERROR_CODE openPort();

	abstract Deady.ERROR_CODE closePort();

	abstract boolean isPortOpen();

	abstract Deady.ERROR_CODE writeData(Vector<Byte> paramVector);

	protected abstract Deady.ERROR_CODE writeDataImmediately(
			Vector<Byte> paramVector);

	Port(DeviceParameters parameters) {
		this.m_deviceParameters = parameters;
		this.m_expectedReceiveType = Deady.DATA_TYPE.GENERAL;

		this.m_RealtimeStatus = 0;
		this.m_RealtimeStatusReady = Boolean.valueOf(false);
		this.m_MICRDataReady = Boolean.valueOf(false);
		this.m_ImageDataReady = Boolean.valueOf(false);

		this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
		this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_INITSTATE;
		this.m_receiveCounter = 0L;
		this.m_dataBlockSize = 0;
		this.m_identifier = 0;
		this.m_identificationStatus = 0;
	}

	public Deady.ERROR_CODE registerCallback(CallbackInterface callback) {
		Deady.ERROR_CODE retval = Deady.ERROR_CODE.SUCCESS;

		if (callback != null) {
			this.m_callback = callback;
		} else {
			retval = Deady.ERROR_CODE.INVALID_CALLBACK_OBJECT;
		}

		return retval;
	}

	private Vector<Byte> getData(Vector<Byte> source, ReentrantLock sourceLock) {
		Vector<Byte> data = null;

		if (source.size() > 0) {
			sourceLock.lock();
			try {
				data = new Vector<Byte>(source);

				source.clear();
			} catch (Exception e) {
				logger.error("Port.getData",
						"Exception occured: " + e.getMessage());
			} finally {
				sourceLock.unlock();
			}
		}

		return data;
	}

	private byte[] getDataBytes(Vector<Byte> source, ReentrantLock sourceLock) {
		byte[] data = (byte[]) null;

		if (source.size() > 0) {
			data = new byte[source.size()];

			sourceLock.lock();
			try {
				for (int i = 0; i < source.size(); i++) {
					data[i] = ((Byte) source.get(i)).byteValue();
				}

				source.clear();
			} catch (Exception e) {
				logger.error("Port.getDataBytes",
						"Exception occured: " + e.getMessage());
			} finally {
				sourceLock.unlock();
			}
		}

		return data;
	}

	public Vector<Byte> readData() {
		return getData(this.m_receiveBuffer, this.m_receiveBufferLock);
	}

	public Vector<Byte> getASB() {
		return getData(this.m_ASB, this.m_ASBLock);
	}

	public Boolean isRealtimeStatusAvailable() {
		return this.m_RealtimeStatusReady;
	}

	public Deady.ERROR_CODE getError() {
		return this.m_Error;
	}

	public Boolean isMICRStringAvailable() {
		return this.m_MICRDataReady;
	}

	public Boolean isImageDataAvailable() {
		return this.m_ImageDataReady;
	}

	public byte getRealtimeStatus() {
		byte retval = -1;

		if (this.m_RealtimeStatusReady.booleanValue()) {
			retval = this.m_RealtimeStatus;
			this.m_RealtimeStatusReady = Boolean.valueOf(false);
		}

		return retval;
	}

	public String getMICR() {
		byte[] MICRBytes = new byte[100];

		this.m_MICRStringLock.lock();
		String retval;
		try {
			for (int i = 0; i < this.m_MICRString.size(); i++) {
				if ((((Byte) this.m_MICRString.elementAt(i)).byteValue() > 0)
						&& (((Byte) this.m_MICRString.elementAt(i)).byteValue() < 32)) {
					MICRBytes[i] = 32;
				} else {
					MICRBytes[i] = ((Byte) this.m_MICRString.elementAt(i))
							.byteValue();
				}
			}
			retval = new String(MICRBytes);
			retval = retval.substring(0, this.m_MICRString.size() - 1);
		} finally {
			this.m_MICRStringLock.unlock();
		}

		return retval;
	}

	public Vector<Byte> getImageData() {
		return getData(this.m_imageBuffer, this.m_imageBufferLock);
	}

	public byte[] getImageDataBytes() {
		return getDataBytes(this.m_imageBuffer, this.m_imageBufferLock);
	}

	protected void parseOutgoingData(Vector<Byte> data) {
		this.m_RealtimeStatusReady = Boolean.valueOf(false);

		if ((data.size() > 2)
				&& (((Byte) data.get(0)).byteValue() == Deady.ASCII_CONTROL_CODE.DLE
						.getASCIIValue())
				&& (((Byte) data.get(1)).byteValue() == Deady.ASCII_CONTROL_CODE.EOT
						.getASCIIValue())) {
			this.m_expectedReceiveType = Deady.DATA_TYPE.DEVICESTATUS;
		} else if ((data.size() > 3)
				&& (((Byte) data.get(0)).byteValue() == Deady.ASCII_CONTROL_CODE.FS
						.getASCIIValue())
				&& (((Byte) data.get(1)).byteValue() == 97)
				&& (((Byte) data.get(2)).byteValue() == 48)) {
			this.m_expectedReceiveType = Deady.DATA_TYPE.MICR;
			this.m_MICRDataReady = Boolean.valueOf(false);
		}
	}

	protected void afterCallbackAction(Deady.DATA_TYPE dataType) {
		if (dataType == Deady.DATA_TYPE.IMAGE) {
			String command = "FS a '2'";
			Vector<Byte> binaryData = PrinterTools
					.convertEscposToBinary(command);
			writeDataImmediately(binaryData);

			command = "GS ( G 2 0 85 48";
			binaryData = PrinterTools.convertEscposToBinary(command);
			writeDataImmediately(binaryData);
		}
	}

	protected Deady.ERROR_CODE saveData(Vector<Byte> receivedData) {
		byte oneByte = 0;
		int dataCounter = 0;
		int dataSize = 0;
		Boolean blockEndFlag = Boolean.valueOf(false);
		int receivedDataLength = 0;

		byte MICR_ORMask = 121;
		byte MICR_ANDMask = 80;

		byte bACK = Deady.ASCII_CONTROL_CODE.ACK.getASCIIValue();
		Vector<Byte> v_ACK = new Vector<Byte>(1);
		v_ACK.add(new Byte(bACK));

		receivedDataLength = receivedData.size();
		if (receivedDataLength == 0) {
			return Deady.ERROR_CODE.FAILED;
		}

		do {
			switch (this.m_receiveState.ordinal()) {
			case 1:
				oneByte = ((Byte) receivedData.get(0)).byteValue();
				this.m_headerByte = oneByte;

				if ((oneByte & 0x93) == 18) {
					this.m_RealtimeStatus = oneByte;

					this.m_expectedReceiveType = Deady.DATA_TYPE.GENERAL;
					this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.DEVICESTATUS;
				} else if ((oneByte & 0x93) == 16) {
					this.m_ASBLock.lock();
					try {
						this.m_ASB.clear();
						this.m_ASB.add((Byte) receivedData.get(0));
					} finally {
						this.m_ASBLock.unlock();
					}
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_ASB;
				} else if (oneByte == 83) {
					this.m_dataBlockSize = 0;
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_BLOCK_BINARY;
				} else if ((oneByte & 0x91) == 17) {
					if (this.m_receiveSubState == Deady.RECEIVESUBSTATE.RSUBSTATE_INITSTATE) {
						this.m_imageBufferLock.lock();
						this.m_fileInfoBlock.clear();
						this.m_sizeInfoBlock.clear();
						this.m_imageDataBlock.clear();
						this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_FILEINFO;
					}
					this.m_dataBlockSize = 0;
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_BLOCK_HEXADECIMAL;
				} else {
					this.m_receiveBufferLock.lock();
					try {
						this.m_receiveBuffer.add((Byte) receivedData.get(0));
					} finally {
						this.m_receiveBufferLock.unlock();
					}
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
					this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.GENERAL;
				}

				receivedData.remove(0);
				this.m_receiveCounter = 1L;
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				dataCounter = Math.min(4 - this.m_ASB.size(),
						receivedData.size());
				this.m_ASBLock.lock();
				try {
					this.m_ASB.addAll(receivedData.subList(0, dataCounter));
				} finally {
					this.m_ASBLock.unlock();
				}
				receivedData.subList(0, dataCounter).clear();
				if (this.m_ASB.size() != 4)
					continue;
				this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
				this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.ASB;

				break;
			case 5:
				switch (this.m_headerByte) {
				case 55:
					if (this.m_receiveCounter == 1L) {
						this.m_identifier = ((Byte) receivedData.get(0))
								.byteValue();
						receivedData.remove(0);
						this.m_receiveCounter += 1L;

						if ((this.m_identifier == 116)
								|| (this.m_identifier == 42)) {
							continue;
						}
						this.m_receiveBufferLock.lock();
						try {
							this.m_receiveBuffer
									.add(new Byte(this.m_headerByte));
							this.m_receiveBuffer
									.add(new Byte(this.m_identifier));
						} finally {
							this.m_receiveBufferLock.unlock();
						}

						dataCounter = 0;
						dataSize = receivedData.size();
						while ((dataSize > 0)
								&& (((Byte) receivedData.get(dataCounter))
										.byteValue() != 0)) {
							dataCounter++;
							dataSize--;
						}

						this.m_receiveBufferLock.lock();
						try {
							this.m_receiveBuffer.addAll(receivedData.subList(0,
									dataCounter));
						} finally {
							this.m_receiveBufferLock.unlock();
						}

						if (((Byte) receivedData.get(dataCounter)).byteValue() == 0) {
							this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						}

						receivedData.subList(0, dataCounter).clear();
						this.m_receiveCounter += dataCounter;

						continue;
					}
					if (this.m_receiveCounter == 2L) {
						this.m_identificationStatus = ((Byte) receivedData
								.get(0)).byteValue();
						receivedData.remove(0);
						this.m_receiveCounter += 1L;

						if (this.m_identifier != 42)
							continue;
						this.m_MICRStringLock.lock();
						try {
							this.m_MICRString.clear();
						} finally {
							this.m_MICRStringLock.unlock();
						}
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_MICR;

						continue;
					}
					if (this.m_receiveCounter <= 2L)
						continue;
					switch (this.m_receiveSubState.ordinal()) {
					case 3:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						blockEndFlag = Boolean.valueOf(false);
						dataCounter = 0;
						dataSize = receivedData.size();

						while ((dataSize > 0)
								&& (((Byte) receivedData.get(dataCounter))
										.byteValue() != 0)) {
							dataCounter++;
							dataSize--;
						}

						if (((Byte) receivedData.get(dataCounter)).byteValue() == 0) {
							dataCounter++;
							dataSize--;
							blockEndFlag = Boolean.valueOf(true);
						}

						this.m_fileInfoBlock.addAll(receivedData.subList(0,
								dataCounter));
						receivedData.subList(0, dataCounter).clear();

						this.m_receiveCounter += dataCounter;

						if (!blockEndFlag.booleanValue())
							continue;
						this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_SIZEINFO;
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 4:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						blockEndFlag = Boolean.valueOf(false);
						dataCounter = 0;
						dataSize = receivedData.size();

						while ((dataSize > 0)
								&& (((Byte) receivedData.get(dataCounter))
										.byteValue() != 0)) {
							dataCounter++;
							dataSize--;
						}

						if (((Byte) receivedData.get(dataCounter)).byteValue() == 0) {
							dataCounter++;
							dataSize--;
							blockEndFlag = Boolean.valueOf(true);
						}

						this.m_sizeInfoBlock.addAll(receivedData.subList(0,
								dataCounter));
						receivedData.subList(0, dataCounter).clear();

						this.m_receiveCounter += dataCounter;

						if (!blockEndFlag.booleanValue())
							continue;
						this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_IMAGEDATA;
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 5:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						blockEndFlag = Boolean.valueOf(false);
						dataCounter = 0;
						dataSize = receivedData.size();
						dataCounter = receivedData.indexOf(new Byte((byte) 0));
						if (dataCounter != -1) {
							blockEndFlag = Boolean.valueOf(true);
						}

						if (blockEndFlag.booleanValue()) {
							this.m_imageDataBlock.addAll(receivedData.subList(
									0, dataCounter));
							receivedData.subList(0, dataCounter + 1).clear();
							this.m_receiveCounter = (this.m_receiveCounter
									+ dataCounter + 1L);
						} else {
							this.m_imageDataBlock.addAll(receivedData.subList(
									0, dataSize));
							receivedData.subList(0, dataSize).clear();
							this.m_receiveCounter += dataSize;
						}

						if (!blockEndFlag.booleanValue())
							continue;
						if ((this.m_identificationStatus == 64)
								|| (this.m_identificationStatus == 66)) {
							this.m_imageBufferLock.lock();
							try {
								this.m_imageBuffer = PrinterTools
										.convertHexadecimalToBinary(this.m_imageDataBlock);
							} finally {
								this.m_imageBufferLock.unlock();
							}
							this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.IMAGE;

							this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_INITSTATE;
							writeDataImmediately(v_ACK);
						}
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 2:
						break;
					case 6:
						continue;
					case 1:
					}

					break;
				case 49:
				case 57:
				case 91:
				case 95:
					if (this.m_expectedReceiveType != Deady.DATA_TYPE.MICR)
						break;
					if (this.m_receiveCounter != 1L)
						break;
					this.m_identificationStatus = ((Byte) receivedData
							.elementAt(0)).byteValue();
					if ((this.m_identificationStatus | MICR_ORMask) != MICR_ORMask)
						break;
					if ((this.m_identificationStatus & MICR_ANDMask) != MICR_ANDMask)
						break;
					receivedData.remove(0);
					this.m_receiveCounter += 1L;
					this.m_MICRStringLock.lock();
					try {
						this.m_MICRString.clear();
					} finally {
						this.m_MICRStringLock.unlock();
					}
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_MICR;
					break;
				}

				this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.GENERAL;
				dataCounter = 0;
				dataSize = receivedData.size();

				while ((dataSize > 0)
						&& (((Byte) receivedData.get(dataCounter)).byteValue() != 0)) {
					dataCounter++;
					dataSize--;
				}

				if (((Byte) receivedData.get(dataCounter)).byteValue() == 0) {
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
				}

				this.m_receiveBufferLock.lock();
				try {
					this.m_receiveBuffer.addAll(receivedData.subList(0,
							dataCounter));
				} finally {
					this.m_receiveBufferLock.unlock();
				}

				receivedData.subList(0, dataCounter).clear();
				this.m_receiveCounter += dataCounter;

				break;
			case 6:
				if (this.m_receiveCounter == 1L) {
					this.m_identifier = ((Byte) receivedData.elementAt(0))
							.byteValue();
					receivedData.remove(0);
					this.m_receiveCounter += 1L;

					if (this.m_identifier != 32)
						continue;
					this.m_fileInfoBlock.clear();
					this.m_sizeInfoBlock.clear();
					this.m_imageDataBlock.clear();
					this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_FILEINFO;
				} else {
					if (this.m_identifier == 32) {
						if (this.m_receiveCounter == 2L) {
							this.m_identificationStatus = ((Byte) receivedData
									.elementAt(0)).byteValue();
							receivedData.remove(0);
							this.m_receiveCounter += 1L;
							continue;
						}
						if (this.m_receiveCounter == 3L) {
							this.m_dataBlockSize = ((Byte) receivedData
									.elementAt(0)).byteValue();
							receivedData.remove(0);
							this.m_receiveCounter += 1L;
							continue;
						}
						if (this.m_receiveCounter == 4L) {
							this.m_dataBlockSize += ((Byte) receivedData
									.elementAt(0)).byteValue() * 256;
							receivedData.remove(0);
							this.m_receiveCounter += 1L;
							continue;
						}

					}

					switch (this.m_receiveSubState.ordinal()) {
					case 3:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						dataCounter = Math.min(receivedData.size(),
								this.m_dataBlockSize);
						this.m_fileInfoBlock.addAll(receivedData.subList(0,
								dataCounter));
						receivedData.subList(0, dataCounter).clear();

						this.m_dataBlockSize -= dataCounter;
						this.m_receiveCounter += dataCounter;

						if (this.m_dataBlockSize != 0)
							continue;
						this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_SIZEINFO;
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 4:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						dataCounter = Math.min(receivedData.size(),
								this.m_dataBlockSize);
						this.m_sizeInfoBlock.addAll(receivedData.subList(0,
								dataCounter));
						receivedData.subList(0, dataCounter).clear();

						this.m_dataBlockSize -= dataCounter;
						this.m_receiveCounter += dataCounter;

						if (this.m_dataBlockSize != 0)
							continue;
						this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_SIZEINFO;
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 5:
						this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.NOTHING;
						dataCounter = Math.min(receivedData.size(),
								this.m_dataBlockSize);
						this.m_imageDataBlock.addAll(receivedData.subList(0,
								dataCounter));
						receivedData.subList(0, dataCounter).clear();
						this.m_dataBlockSize -= dataCounter;
						this.m_receiveCounter += dataCounter;

						if (this.m_dataBlockSize != 0)
							continue;
						if ((this.m_identificationStatus == 64)
								|| (this.m_identificationStatus == 66)) {
							this.m_imageBufferLock.lock();
							try {
								this.m_imageBuffer = ((Vector<Byte>) this.m_imageDataBlock
										.clone());
							} finally {
								this.m_imageBufferLock.unlock();
							}
							this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.IMAGE;
							this.m_imageDataBlock.clear();
							this.m_receiveSubState = Deady.RECEIVESUBSTATE.RSUBSTATE_INITSTATE;
							this.m_expectedReceiveType = Deady.DATA_TYPE.GENERAL;
							writeDataImmediately(v_ACK);
						}
						this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
						writeDataImmediately(v_ACK);

						break;
					case 2:
						break;
					case 6:
					case 1:
					}
				}

				break;
			case 7:
				dataCounter = 0;
				dataSize = receivedData.size();

				while ((dataSize > 0)
						&& (((Byte) receivedData.get(dataCounter)).byteValue() != 0)) {
					dataCounter++;
					dataSize--;
				}

				this.m_MICRStringLock.lock();
				try {
					this.m_MICRString.addAll(receivedData.subList(0,
							dataCounter + 1));
				} finally {
					this.m_MICRStringLock.unlock();
				}

				if (((Byte) receivedData.get(dataCounter)).byteValue() == 0) {
					this.m_receiveState = Deady.RECEIVESTATE.RSTATE_SINGLE;
					this.m_callbackInfo.ReceivedDataType = Deady.DATA_TYPE.MICR;
					this.m_expectedReceiveType = Deady.DATA_TYPE.GENERAL;
				}
				receivedData.subList(0, dataCounter + 1).clear();
				break;
			case 9:
			case 8:
			}
		} while (receivedData.size() > 0);

		switch (this.m_callbackInfo.ReceivedDataType.ordinal()) {
		case 4:
			logger.info("Port", "m_RealtimeStatusReady=true");
			this.m_RealtimeStatusReady = Boolean.valueOf(true);
			break;
		case 2:
			logger.info("Port", "m_MICRDataReady=true");
			this.m_MICRDataReady = Boolean.valueOf(true);
			break;
		case 3:
			logger.info("Port", "m_ImageDataReady=true");
			this.m_ImageDataReady = Boolean.valueOf(true);
			break;
		case 5:
			logger.info("Port", "ASB data ready");
			break;
		case 1:
			logger.info("Port", "GENERAL data ready");
			break;
		}

		return Deady.ERROR_CODE.SUCCESS;
	}
}