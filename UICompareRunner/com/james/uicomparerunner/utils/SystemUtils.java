
package com.james.uicomparerunner.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.james.uicomparerunner.UICompareRunner;
import com.james.uicomparerunner.res.R;

public class SystemUtils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public interface OnExecCallBack {
		public void onExec(String response, String error);
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

	public static void exec(String cmd, OnExecCallBack onExecCallBack) {
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
				onExecCallBack.onExec(response, error);
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void restart() throws URISyntaxException, IOException {
		final String javaBin = System.getProperty("java.home") + File.separator +
				"bin" + File.separator + "java";
		final File currentJar = new
				File(UICompareRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}

}
