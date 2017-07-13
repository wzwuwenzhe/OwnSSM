package com.deady.mvc.exception;

public class NoNumberException extends RuntimeException {

	public NoNumberException(String message) {
		super(message);
	}

	public NoNumberException(String message, Throwable cause) {
		super(message, cause);
	}

}