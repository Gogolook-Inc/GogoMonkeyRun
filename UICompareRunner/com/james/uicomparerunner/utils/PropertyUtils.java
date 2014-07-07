
package com.james.uicomparerunner.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtils {
	public static final String NULL = "none";

	public static final String KEY_VERSION = "version";
	public static final String KEY_DEVICE = "device";
	public static final String KEY_SDK_PATH = "sdk_path";
	public static final String KEY_LAST_SCRIPT = "last_script";
	public static final String KEY_FROM_EMAIL = "from_mail";
	public static final String KEY_FROM_EMAIL_PASSWORD = "from_mail_password";

	private static Properties getProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		if (!new File("config.properties").exists())
			return prop;

		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			return prop;

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static void saveProperty(String key, String value) {
		Properties prop = getProperties();
		OutputStream output = null;

		try {
			output = new FileOutputStream("config.properties");
			// set the properties value
			prop.setProperty(key, value);
			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static String loadProperty(String key, String defaultValue) {
		Properties prop = new Properties();
		InputStream input = null;

		if (!new File("config.properties").exists())
			return defaultValue;

		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			String value = prop.getProperty(key);
			return value == null ? NULL : value;

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return defaultValue;
	}

	public static boolean existProperty(String key) {
		if (loadProperty(key, NULL).equalsIgnoreCase(NULL))
			return false;

		return true;
	}

	public static void deleteProperty(String key) {
		saveProperty(key, NULL);
	}
}
