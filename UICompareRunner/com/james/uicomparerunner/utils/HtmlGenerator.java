
package com.james.uicomparerunner.utils;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import com.james.uicomparerunner.UICompareRunner;

public class HtmlGenerator {

	public static void createHtml(int imageWidth, int imageHeight) throws IOException {
		File targetDir = new File(UICompareRunner.dir_device_target_picture);
		File testDir = new File(UICompareRunner.dir_device_test_picture);

		File htmlDir = new File(UICompareRunner.dir_device_picture);
		long time = new Date().getTime();
		File htmlPath = new File(htmlDir.getParentFile().getAbsolutePath() + "/result_" + time + ".html");
		Writer wt = new FileWriter(htmlPath);
		BufferedWriter writer = new BufferedWriter(wt);

		writer.newLine();
		writer.write("<!DOCTYPE html>");
		writer.newLine();
		writer.write("<html>");
		writer.newLine();
		writer.write("	<head>");
		writer.newLine();
		writer.write("		<style>");
		writer.newLine();
		writer.write("			table, th, td {");
		writer.newLine();
		writer.write("				border: 1px solid black;");
		writer.newLine();
		writer.write("			}");
		writer.newLine();
		writer.write("		</style>");
		writer.newLine();
		writer.write("	</head>");
		writer.newLine();

		writer.write("	<body>");
		writer.newLine();
		writer.newLine();
		writer.write("		<h2>UI Compare Result</h2>");
		writer.newLine();
		writer.write("		<table style=\"width:" + imageWidth + "px\">");
		writer.newLine();

		for (String fileName : targetDir.list()) {
			for (String fileName2 : testDir.list()) {
				if (!fileName.equalsIgnoreCase(fileName2))
					continue;
				writer.write("			<tr>");
				writer.newLine();
				writer.write("				<td><img src=\"./sreenshot/target/" + fileName + "\" alt=\"Pulpit rock\" width=\"" + imageWidth + "\" height=\"" + imageHeight + "\"/></td>");
				writer.newLine();
				writer.write("				<td><img src=\"./sreenshot/test/" + fileName + "\" alt=\"Pulpit rock\" width=\"" + imageWidth + "\" height=\"" + imageHeight + "\"/></td>");
				writer.newLine();
				writer.write("				<td><img src=\"./sreenshot/result/" + fileName + "\" alt=\"Pulpit rock\" width=\"" + imageWidth + "\" height=\"" + imageHeight + "\"/></td>");
				writer.newLine();
				writer.write("			</tr>");
				writer.newLine();
			}
		}

		writer.write("		</table>");
		writer.newLine();

		writer.newLine();
		writer.write("	</body>");
		writer.newLine();
		writer.write("</html>");
		writer.newLine();
		writer.close();

		Desktop.getDesktop().browse(htmlPath.toURI());
	}
}
