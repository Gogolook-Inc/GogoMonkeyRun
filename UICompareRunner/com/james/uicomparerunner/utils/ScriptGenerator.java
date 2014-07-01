
package com.james.uicomparerunner.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.james.uicomparerunner.UICompareRunner;

public class ScriptGenerator {

	public final static String START_FROM_DESKTOP = "START FROM DESKTOP";
	public final static String END_BACK_TO_DESKTOP = "END BACK TO DESKTOP";
	public final static String CLEAR_DATA = "CLEAR DATA";

	private static int index = 0;
	private static int loopCount = 1;

	public static String getScriptFilePath(String mrPath) throws IOException {
		if (mrPath == null)
			return null;

		String mrFileName = new File(mrPath).getName();

		index = 0;
		//
		String scriptFilePath = UICompareRunner.dir_device_script + "/" + mrFileName.replace(".mr", ".py");
		Writer wt = new FileWriter(new File(scriptFilePath));
		BufferedWriter writer = new BufferedWriter(wt);

		writer.newLine();
		writer.write("import sys,traceback");
		writer.newLine();
		writer.write("from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice, MonkeyImage");
		writer.newLine();
		writer.newLine();
		writer.write("def getDevice():");
		writer.newLine();
		writer.write("	return sys.argv[1]");
		writer.newLine();
		writer.newLine();
		writer.write("def getPath():");
		writer.newLine();
		writer.write("	return sys.argv[2]");
		writer.newLine();
		writer.newLine();

		//
		Reader rd = new FileReader(mrPath);
		BufferedReader reader = new BufferedReader(rd);

		generateSteps(reader, writer);

		writer.newLine();
		writer.write("print(\"get device name\")");
		writer.newLine();
		writer.write("device_name = getDevice()");
		writer.newLine();
		writer.newLine();
		writer.write("print(\"get dir of screenshot\")");
		writer.newLine();
		writer.write("dir_picture = getPath()");
		writer.newLine();
		writer.newLine();
		writer.write("print(\"Connects to the current device, returning a MonkeyDevice object\")");
		writer.newLine();
		writer.write("device = MonkeyRunner.waitForConnection(5,device_name)");
		writer.newLine();
		writer.newLine();
		writer.write("if not device:");
		writer.newLine();
		writer.write("	print(\"device connect...fail\")");
		writer.newLine();
		writer.write("	sys.exit(1)");
		writer.newLine();
		writer.write("else:");
		writer.newLine();
		writer.write("	print(\"device connect...success\")");
		writer.newLine();
		writer.write("	startSteps()");
		writer.newLine();
		writer.write("	device.shell(\"stop\")");
		writer.newLine();
		writer.write("	sys.exit(0)");
		writer.newLine();
		writer.newLine();

		writer.close();
		reader.close();

		return scriptFilePath;
	}

	private static void generateSteps(BufferedReader reader, BufferedWriter writer) throws IOException {
		//
		writer.write("def startSteps():");
		writer.newLine();
		writer.write("	print (\"start monkey runner.\")");
		writer.newLine();

		String line = null;
		while ((line = reader.readLine()) != null) {
			String tab = "	";
			for (int i = 1; i < loopCount; i++) {
				tab = tab + "	";
			}

			if (!line.startsWith("#"))
				writer.write(tab + "print(\"" + line + "\")");
			writer.newLine();
			//
			if (line.startsWith("#")) {
				writer.write(tab + line);
				writer.newLine();
			}
			else if (line.startsWith(CLEAR_DATA)) {
				writer.write(tab + "device.shell('pm clear " + UICompareRunner.package_name + "')");
				writer.newLine();
				writer.write(tab + "MonkeyRunner.sleep(4.0)");
				writer.newLine();
				writer.newLine();
			}
			else if (line.startsWith(START_FROM_DESKTOP) || line.startsWith(END_BACK_TO_DESKTOP)) {
				writer.write(tab + "device.shell('am force-stop " + UICompareRunner.package_name + "')");
				writer.newLine();
				writer.write(tab + "MonkeyRunner.sleep(4.0)");
				writer.newLine();
				writer.newLine();
			}
			else if (line.startsWith("TOUCH")) {
				String str = line.replace("TOUCH|", "");
				Pattern pattern = Pattern.compile("'x':(\\d+),'y':(\\d+),'type':'(\\S+)',");
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					String action = "MonkeyDevice.DOWN_AND_UP";
					writer.write(tab + "device.touch(" + matcher.group(1) + "," + matcher.group(2) + "," + action + ")");
					writer.newLine();
					writer.write(tab + "MonkeyRunner.sleep(4.0)");
					writer.newLine();
					writer.newLine();
				}
			}
			else if (line.startsWith("DRAG")) { // DRAG|{'start':(288,947),'end':(288,189),'duration':1.0,'steps':2,}
				String str = line.replace("DRAG|", "");
				Pattern pattern = Pattern.compile("'start':\\((\\d+),(\\d+)\\),'end':\\((\\d+),(\\d+)\\),'duration':(\\d+\\S+\\d+),'steps':(\\d+),");
				Matcher matcher = pattern.matcher(str);

				if (matcher.find()) {
					writer.write(tab + "start = (" + matcher.group(1) + "," + matcher.group(2) + ")");
					writer.newLine();
					writer.write(tab + "end = (" + matcher.group(3) + "," + matcher.group(4) + ")");
					writer.newLine();
					writer.write(tab + "device.drag(start,end," + matcher.group(5) + "," + matcher.group(6) + ")");
					writer.newLine();
					writer.write(tab + "MonkeyRunner.sleep(4.0)");
					writer.newLine();
					writer.newLine();
				}
			}
			else if (line.startsWith("PRESS")) {
				String str = line.replace("PRESS|", "");
				Pattern pattern = Pattern.compile("'name':'(\\S+)','type':'(\\S+)',");
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					String action = "MonkeyDevice.DOWN_AND_UP";
					writer.write(tab + "device.press('KEYCODE_" + matcher.group(1) + "'," + action + ")");
					writer.newLine();
					writer.write(tab + "MonkeyRunner.sleep(4.0)");
					writer.newLine();
					writer.newLine();
				}
			}
			else if (line.startsWith("TYPE")) {
				String str = line.replace("TYPE|", "");
				Pattern pattern = Pattern.compile("'message':'(\\S+)',");
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					writer.write(tab + "device.type('" + matcher.group(1) + "')");
					writer.newLine();
					writer.newLine();
				}
			}
			else if (line.startsWith("WAIT")) {
				String str = line.replace("WAIT|", "");
				Pattern pattern = Pattern.compile("'seconds':(\\d+\\S+\\d+),");
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					writer.write(tab + "MonkeyRunner.sleep(" + matcher.group(1) + ")");
					writer.newLine();
				}
			}
			else if (line.startsWith("TAKE SNAPSHOT")) {
				snapshot(writer, tab);
			}
			else if (line.startsWith("LOOP")) {
				String count = line.replace("LOOP(", "").replace(")", "");
				writer.write(tab + "for index in range(" + count + "):");
				writer.newLine();
				loopCount++;
			}
			else if (line.startsWith("END LOOP")) {
				writer.newLine();
				loopCount--;
			}
		}
		writer.write("	print(\"end monkey runner.\")");
		writer.newLine();
	}

	private static void snapshot(BufferedWriter writer, String append) throws IOException {
		writer.write(append + "result = device.takeSnapshot()");
		writer.newLine();
		writer.write(append + "print(\"Writes the screenshot to a file\")");
		writer.newLine();
		if (append.equalsIgnoreCase("	")) {
			writer.write(append + "result.writeToFile(dir_picture+'/screenshot_'+str(0)+'_" + index + ".png','png')");
		}
		else {
			writer.write(append + "result.writeToFile(dir_picture+'/screenshot_'+str(index)+'_" + index + ".png','png')");
		}
		writer.newLine();
		writer.newLine();

		index++;
	}
}
