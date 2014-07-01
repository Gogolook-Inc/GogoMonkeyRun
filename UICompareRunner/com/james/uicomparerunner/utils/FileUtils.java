
package com.james.uicomparerunner.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	public static void deletePicturesInDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					continue;
				}
				else {
					files[i].delete();
				}
			}
		}
	}

	public static void copyFilesFromDirToDir(String originalDir, String targetDir) {
		// origin
		if (!originalDir.endsWith("/")) {
			originalDir = originalDir + "/";
		}

		//
		File originalFile = new File(originalDir);
		if (!originalFile.exists()) {
			System.out.println(originalFile.getAbsolutePath() + " does not exist.");
			return;
		}

		// target
		if (!targetDir.endsWith("/")) {
			targetDir = targetDir + "/";
		}

		//
		File targetFile = new File(targetDir);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}

		//
		String[] files = null;
		files = originalFile.list();

		for (String filename : files) {
			if (!filename.endsWith(".png"))
				continue;

			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(originalDir + filename);
				out = new FileOutputStream(targetDir + filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static void copyFileFromFileToFile(File fromFile, File toFile) throws IOException {
		FileInputStream in = new FileInputStream(fromFile);
		FileOutputStream out = new FileOutputStream(toFile);
		copyFile(in, out);

		in.close();
		in = null;
		out.flush();
		out.close();
		out = null;
	}

	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
