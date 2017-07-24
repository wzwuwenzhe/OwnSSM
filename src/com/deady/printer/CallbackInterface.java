package com.deady.printer;

public interface CallbackInterface {

	public abstract Deady.ERROR_CODE CallbackMethod(
			DeadyCallbackInfo paramEpsonComCallbackInfo);
}
