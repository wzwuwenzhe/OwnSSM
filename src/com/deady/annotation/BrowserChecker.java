package com.deady.annotation;

public @interface BrowserChecker {

	boolean checkBrowserVersion() default true; // 是否检查浏览器版本
}
