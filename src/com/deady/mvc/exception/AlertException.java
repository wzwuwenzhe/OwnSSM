package com.deady.mvc.exception;

/**
 * @author Andre.Z 2014-11-3 下午11:25:54<br>
 * 
 */
@SuppressWarnings("serial")
public class AlertException extends DeadyException {

	public AlertException(String string) {
		super(string);
	}

	@Override
	public String getCode() {
		return ExceptionCode.Alert.getCode();
	}

}
