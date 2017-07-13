package com.deady.mvc.exception;

@SuppressWarnings("serial")
public abstract class DeadyException extends Exception {

	public DeadyException(String string) {
		super(string);
	}

	public abstract String getCode();

}
