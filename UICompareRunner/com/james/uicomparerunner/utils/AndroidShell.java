
package com.james.uicomparerunner.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AndroidShell {
	private String adbPath;
	private ProcessBuilder builder;
	private Process adb;
	private static final byte[] LS = "\n".getBytes();

	private OutputStream processInput;
	private InputStream processOutput;

	private Thread t;

	private OnShellExecCallBack mOnShellExecCallBack;

	public interface OnExitCallBack {
		public void onExit();
	}

	public interface OnShellExecCallBack {
		public void onExec(String response);
	}

	public AndroidShell(String adbPath) {
		this.adbPath = adbPath;
	}

	/**
	 * Starts the shell
	 */
	public void start() throws IOException {
		String deviceName = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		builder = new ProcessBuilder(adbPath, "-s", deviceName, "shell");
		adb = builder.start();

		// reads from the process output
		processInput = adb.getOutputStream();

		// sends to process's input
		processOutput = adb.getInputStream();

		// thread that reads process's output and prints it to system.out
		Thread t = new Thread() {
			public void run() {
				try {
					int c = 0;
					byte[] buffer = new byte[2048];
					while ((c = processOutput.read(buffer)) != -1) {
						String response = new String(buffer, 0, c);

						ArrayList<String> lines = new ArrayList<String>(Arrays.asList(response.split("\n")));
						for (String line : lines) {
							System.out.println("line: " + line);

							if (!line.startsWith("shell") && mOnShellExecCallBack != null)
								mOnShellExecCallBack.onExec(line);
						}
					}
				} catch (Exception e) {
				}
			}
		};
		t.start();
	}

	/**
	 * Stop the shell;
	 */
	public void stop(OnExitCallBack onExitCallBack) {
		try {
			if (processOutput != null && t != null) {
				this.exec("exit");
				processOutput.close();
			}
		} catch (Exception ignore) {
		}

		if (onExitCallBack != null) {
			onExitCallBack.onExit();
		}
	}

	public void exec(String adbCommand) throws IOException {
		exec(adbCommand, null);
	}

	/**
	 * Executes a command on the shell
	 * 
	 * @param adbCommand the command line. e.g.
	 *            "am start -a android.intent.action.MAIN -n com.q.me.fui.activity/.InitActivity"
	 */
	public void exec(String adbCommand, OnShellExecCallBack onShellExecCallBack) throws IOException {
		mOnShellExecCallBack = onShellExecCallBack;

		processInput.write(adbCommand.getBytes());
		processInput.write(LS);
		processInput.flush();
	}
}
