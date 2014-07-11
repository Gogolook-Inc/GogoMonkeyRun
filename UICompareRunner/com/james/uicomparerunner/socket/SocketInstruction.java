
package com.james.uicomparerunner.socket;

public class SocketInstruction {
	public static final String separator = Character.toString((char) 11);

	public static final String SOCKET_CONNECTED = "_SOCKET_CONNECTED";
	public static final String SOCKET_CLOSE = "_SOCKET_CLOSE";
	public static final String GET_ALL_DEVICES = "_GET_ALL_DEVICES";
	public static final String SET_DEVICE = "_SET_DEVICE";
	public static final String GET_ALL_SCRIPTS = "_GET_ALL_SCRIPTS";
	public static final String SET_SCRIPT = "_SET_SCRIPT";
	public static final String COMPLETE_RUNNING_SCRIPT = "_COMPLETE_RUNNING_SCRIPT";
	public static final String GET_ALL_APKS = "_GET_ALL_APKS";
	public static final String SET_APK = "_SET_APK";
	public static final String COMPLETE_INSTALLING_APK = "_COMPLETE_INSTALLING_APK";
	public static final String ERROR_MESSAGE = "_ERROR_MESSAGE";
}
