package android.util;

public final class Log {
	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;

	Log() {
		throw new RuntimeException("Stub!");
	}

	public static int v(String tag, String msg) {
		throw new RuntimeException("Stub!");
	}

	public static int v(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int d(String tag, String msg) {
		System.out.println(tag + msg);
		return 0;
	}

	public static int d(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int i(String tag, String msg) {
		throw new RuntimeException("Stub!");
	}

	public static int i(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int w(String tag, String msg) {
		throw new RuntimeException("Stub!");
	}

	public static int w(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static native boolean isLoggable(String paramString, int paramInt);

	public static int w(String tag, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int e(String tag, String msg) {
		throw new RuntimeException("Stub!");
	}

	public static int e(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int wtf(String tag, String msg) {
		throw new RuntimeException("Stub!");
	}

	public static int wtf(String tag, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int wtf(String tag, String msg, Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static String getStackTraceString(Throwable tr) {
		throw new RuntimeException("Stub!");
	}

	public static int println(int priority, String tag, String msg) {
		throw new RuntimeException("Stub!");
	}
}