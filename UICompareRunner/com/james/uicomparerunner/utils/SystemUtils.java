
package com.james.uicomparerunner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.james.uicomparerunner.UICompareRunner;
import com.james.uicomparerunner.res.R;

public class SystemUtils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public interface OnExecCallBack {
		public void onExec(Process process, String line);

		public void afterExec(String response, String error);
	}

	public static void init() {

		if (isMac()) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", R.string.app_name);
		}

		try {
		} catch (Exception e) {
			System.out.println("Error setting Java LAF: " + e);
			UICompareRunner.setLabelText("Error setting Java LAF: " + e);
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static void exec(final String cmd, final OnExecCallBack onExecCallBack) {
		String s = null;
		try {

			String[] args = cmd.split(" ");
			final ArrayList<String> command = new ArrayList<String>();
			for (int i = 0; i < args.length; i++) {
				command.add(args[i]);
			}

			final ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the response of the command (if any):");
			String response = "";
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				UICompareRunner.setLabelText(s);
				if (onExecCallBack != null) {
					onExecCallBack.onExec(process, s);
				}
				if (response.equalsIgnoreCase("")) {
					response = s;
				}
				else {
					response = response + "\n" + s;
				}
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):");
			String error = "";
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
				UICompareRunner.setLabelText(s);
				if (error.equalsIgnoreCase("")) {
					error = s;
				}
				else {
					error = error + "\n" + s;
				}
			}

			process.destroy();
			if (onExecCallBack != null)
				onExecCallBack.afterExec(response, error);
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
