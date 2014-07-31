
package com.james.uicomparerunner;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.james.uicomparerunner.c.Constants;
import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.socket.SocketInstruction;
import com.james.uicomparerunner.socket.SocketServer;
import com.james.uicomparerunner.socket.SocketServer.SocketStatusListener;
import com.james.uicomparerunner.ui.RecorderEditFrame;
import com.james.uicomparerunner.ui.ScriptConcatenateFrame;
import com.james.uicomparerunner.ui.SocketFrame;
import com.james.uicomparerunner.ui.UiCompareFrame;
import com.james.uicomparerunner.ui.UiCompareFrame.OnReplaceClickListener;
import com.james.uicomparerunner.ui.dialog.DialogBuilder;
import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;
import com.james.uicomparerunner.utils.EmailUtils;
import com.james.uicomparerunner.utils.FileUtils;
import com.james.uicomparerunner.utils.HtmlGenerator;
import com.james.uicomparerunner.utils.PropertyUtils;
import com.james.uicomparerunner.utils.ScreenUtils;
import com.james.uicomparerunner.utils.ScriptGenerator;
import com.james.uicomparerunner.utils.SystemUtils;
import com.james.uicomparerunner.utils.SystemUtils.OnExecCallBack;

public class UICompareRunner {
	public static String android_sdk_path = null;
	public static String adb = "/platform-tools/adb";
	public static String monkey_runner = "/tools/monkeyrunner";
	public static String monkey_runner_ext_lib = "/tools/lib/monkeyrunner_ext.jar";
	public static String monkey_recorder_file_path = "python/ui_recorder.py";
	public static String monkey_installer_file_path = "python/installer.py";
	public static String dir_device = "%s";
	public static String dir_device_picture = "%s/%s/sreenshot";
	public static String dir_device_target_picture = "%s/%s/sreenshot/target";
	public static String dir_device_test_picture = "%s/%s/sreenshot/test";
	public static String dir_device_result_picture = "%s/%s/sreenshot/result";
	public static String dir_device_script = "%s/script";

	public static String package_name = "gogolook.callgogolook2";

	private static UiCompareFrame uiCompareFrame;

	private static RecorderEditFrame recorderEditFrame;

	private static ScriptConcatenateFrame scriptConcatenateFrame;

	private static SocketFrame socketFrame;

	private static SocketServer socketServer;

	private static String apkName = null;

	public static void main(String args[]) {

		SystemUtils.init();

		initUI();

		setPath();

		setDefaultDevice(false);

		initProperDir(null);

		initSocket();

		setEmailForCrashReport(false);

		showQuikActionSelectDialog(true, R.string.dialog_alert_run_last_script);

		PropertyUtils.saveProperty(PropertyUtils.KEY_VERSION, Constants.VERSION);
	}

	private static void initUI() {
		uiCompareFrame = new UiCompareFrame(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem menuItem = (JMenuItem) e.getSource();
				if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_close)) {
					//
					if (recorderEditFrame != null && recorderEditFrame.isShowing()) {
						recorderEditFrame.dispose();
					}
					else if (scriptConcatenateFrame != null && scriptConcatenateFrame.isShowing()) {
						scriptConcatenateFrame.dispose();
					}
					else if (uiCompareFrame.isEditorShown()) {
						recorderEditFrame.dispose();
					}
					else {
						System.exit(0);
					}
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_generate_script)) {
					//
					recordNewAction();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_edit_recorder)) {
					//
					loadRecorderAndEdit(null);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_choose_script)) {
					//
					loadScriptAndRun(null);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_run_last_script)) {
					//
					startMonkeyRunner();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_show_last_result)) {
					//
					generateResult(false);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_file_clear)) {
					//
					uiCompareFrame.removeAll();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_device_reset_device)) {
					//
					setDefaultDevice(true);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_device_reset_apk)) {
					//
					selectAPKtoInstall();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_device_reset_package_name)) {
					//
					inputPackageName();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_device_random_test)) {
					randomTest();
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_device_report_error)) {
					setEmailForCrashReport(true);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_open_editor)) {
					uiCompareFrame.checkSharedPreference(package_name);
				}
				else if (menuItem.getText().equalsIgnoreCase(R.string.menu_help_screen_shot)) {
					ScreenUtils.capture();
				}
			}
		});

		uiCompareFrame.setOnReplaceClickListener(new OnReplaceClickListener() {

			@Override
			public void onReplace(String origin, String target) {
				int re = DialogBuilder.showConfirmDialog(uiCompareFrame, R.string.dialog_alert_set_as_target);
				if (re != 0)
					return;

				try {
					FileUtils.copyFileFromFileToFile(new File(origin), new File(target));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_set_as_target_success);
				//
				generateResult(false);
			}
		});
	}

	private static void setPath() {
		if (!PropertyUtils.existProperty(PropertyUtils.KEY_SDK_PATH)) {
			DialogBuilder.showSettingSDKPathDialog(uiCompareFrame);
			setPath();
			return;
		}

		android_sdk_path = PropertyUtils.loadProperty(PropertyUtils.KEY_SDK_PATH, PropertyUtils.NULL);

		monkey_runner = new File(android_sdk_path + monkey_runner).getAbsolutePath();
		if (android_sdk_path.equalsIgnoreCase(PropertyUtils.NULL) ||
				(!new File(monkey_runner).exists() && !new File(monkey_runner + ".bat").exists())) {
			monkey_runner = "/tools/monkeyrunner";
			PropertyUtils.deleteProperty(PropertyUtils.KEY_SDK_PATH);
			setPath();
			return;
		}

		// set path of adb.exe
		adb = new File(android_sdk_path + adb).getAbsolutePath();

		// check monkey_runner_ext_lib exist or not
		if (!PropertyUtils.loadProperty(PropertyUtils.KEY_VERSION, "1.0.0").equalsIgnoreCase(Constants.VERSION) || !new File(android_sdk_path + monkey_runner_ext_lib).exists()) {
			try {
				FileUtils.copyFileFromFileToFile(new File("libs/monkeyrunner_ext"), new File(android_sdk_path + monkey_runner_ext_lib));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		//
		monkey_recorder_file_path = new File(monkey_recorder_file_path).getAbsolutePath();
		monkey_installer_file_path = new File(monkey_installer_file_path).getAbsolutePath();
	}

	private static void setDefaultDevice(boolean reset) {
		final String originDevice = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		if (reset)
			PropertyUtils.deleteProperty(PropertyUtils.KEY_DEVICE);

		//
		SystemUtils.exec(adb + " " + "devices", new OnExecCallBack() {

			@Override
			public void onExec(Process process, String line) {

			}

			@Override
			public void afterExec(String response, String error) {
				if (!error.equalsIgnoreCase("")) {
					DialogBuilder.showMessageDialog(uiCompareFrame, error);

					if (socketServer != null && socketServer.isConnected()) {
						socketServer.write(SocketInstruction.ERROR_MESSAGE + SocketInstruction.separator + error);
					}
					return;
				}

				Object[] possibilities = getAllDevices(response);

				if (possibilities == null) {
					return;
				}

				String selectDevice = DialogBuilder.showDeviceSelectDialog(uiCompareFrame, possibilities);
				if (selectDevice != null) {
					PropertyUtils.saveProperty(PropertyUtils.KEY_DEVICE, selectDevice);
				}
				else {
					PropertyUtils.saveProperty(PropertyUtils.KEY_DEVICE, originDevice);
				}
				uiCompareFrame.setDeviceName(selectDevice);
			}
		});
	}

	private static Object[] getAllDevices(String text) {

		String[] splits = text.split("\n");
		ArrayList<String> devices = new ArrayList<String>();
		for (String split : splits) {
			if (split.contains("	")) {
				String device = split.split("	")[0];
				devices.add(device);
			}
		}

		if (devices.contains(PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL))) {
			return null;
		}

		Object[] possibilities = devices.toArray();
		return possibilities;
	}

	private static void recordNewAction() {
		final String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		if (device.equalsIgnoreCase(PropertyUtils.NULL)) {
			setDefaultDevice(true);
			recordNewAction();
			return;
		}

		//
		new Thread(new Runnable() {

			@Override
			public void run() {
				uiCompareFrame.disableMenu();
				JDialog dialog = DialogBuilder.showProgressDialog(uiCompareFrame, "start record actions");
				setLabelText("start record actions...");
				SystemUtils.exec(monkey_runner + " " + monkey_recorder_file_path + " " + device + " " + "record" + " " + dir_device, null);
				dialog.setVisible(false);
				uiCompareFrame.enableMenu();
				//
				setLabelText("stop record actions...");
				try {
					File currentDeviceModelFile = new File("python" + File.separator + "current_devices.txt");
					BufferedReader currentReader = new BufferedReader(new FileReader(currentDeviceModelFile));
					String currentDeviceModel = currentReader.readLine();
					currentReader.close();

					File exceptionDeviceModelsFile = new File("python" + File.separator + "exception_devices.txt");
					FileReader exceptionReader = new FileReader(exceptionDeviceModelsFile);
					BufferedReader exceptionreader = new BufferedReader(exceptionReader);
					String exceptionModels = null;
					String line = null;
					while ((line = exceptionreader.readLine()) != null) {
						if (exceptionModels == null) {
							exceptionModels = "'" + line + "'";
						}
						else {
							exceptionModels = exceptionModels + "," + "'" + line + "'";
						}
					}
					exceptionreader.close();

					if (!exceptionModels.contains(currentDeviceModel)) {
						uiCompareFrame.disableMenu();
						JDialog dialog2 = DialogBuilder.showProgressDialog(uiCompareFrame, "stop record actions");
						SystemUtils.exec(monkey_runner + " " + monkey_recorder_file_path + " " + device + " " + "close" + " " + dir_device, null);
						dialog2.setVisible(false);
						uiCompareFrame.enableMenu();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				generateScript();
			}
		}).start();
	}

	private static void generateScript() {
		setLabelText("generate a script file.");
		String mrPath = DialogBuilder.showFindActionFileDialog(uiCompareFrame);

		if (mrPath == null) {
			setLabelText("cancel generating script.");
			return;
		}

		if (!new File(mrPath).exists()) {
			DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_file_disappear);
			return;
		}
		setLabelText("select file: " + mrPath);

		String monkey_runner_file_path = null;
		try {
			initProperDir(mrPath);
			monkey_runner_file_path = ScriptGenerator.getScriptFilePath(mrPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//
		if (monkey_runner_file_path == null || !new File(monkey_runner_file_path).exists()) {
			return;
		}
		PropertyUtils.saveProperty(PropertyUtils.KEY_LAST_SCRIPT, monkey_runner_file_path);
		uiCompareFrame.setScriptsName(monkey_runner_file_path);

		//
		loadRecorderAndEdit(mrPath);
	}

	private static void loadRecorderAndEdit(String recorderPath) {
		final String mrPath = recorderPath == null ? DialogBuilder.showFindRecorderFileDialog(uiCompareFrame) : recorderPath;
		if (mrPath == null) {
			return;
		}
		if (!new File(mrPath).exists()) {
			DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_file_disappear);
			return;
		}

		recorderEditFrame = new RecorderEditFrame(new OnWindowCloseListener() {

			@Override
			public void onWindowClosing(String... output) {
				loadScriptAndRun(output[0]);
			}
		});

		try {
			recorderEditFrame.setRecorder(mrPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadScriptAndRun(String defaultScriptFile) {
		scriptConcatenateFrame = new ScriptConcatenateFrame(uiCompareFrame, defaultScriptFile, new OnWindowCloseListener() {

			@Override
			public void onWindowClosing(String... output) {
				if (output == null || output.length == 0) {
					return;
				}

				String monkey_runner_file_path = null;
				for (String path : output) {
					if (monkey_runner_file_path == null)
						monkey_runner_file_path = path;
					else
						monkey_runner_file_path = monkey_runner_file_path + "," + path;
				}

				PropertyUtils.saveProperty(PropertyUtils.KEY_LAST_SCRIPT, monkey_runner_file_path);
				uiCompareFrame.setScriptsName(monkey_runner_file_path);

				showQuikActionSelectDialog(false, R.string.dialog_alert_run_script);
			}
		});
	}

	private static void startMonkeyRunner() {
		//
		System.out.println("check point 1");
		final String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		System.out.println("check point 2, device: " + device);
		if (device.equalsIgnoreCase(PropertyUtils.NULL)) {
			System.out.println("check point 3");
			setDefaultDevice(true);
			System.out.println("check point 4");
			startMonkeyRunner();
			System.out.println("check point 5");
			return;
		}

		String path = PropertyUtils.loadProperty(PropertyUtils.KEY_LAST_SCRIPT, PropertyUtils.NULL);
		uiCompareFrame.setScriptsName(path);

		if (path.equalsIgnoreCase(PropertyUtils.NULL)) {
			return;
		}

		final String[] monkey_runner_file_path = path.split(",");

		for (String currentPath : monkey_runner_file_path) {
			if (currentPath == null || !new File(currentPath).exists()) {
				DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_file_disappear);
				if (socketServer != null && socketServer.isConnected()) {
					socketServer.write(SocketInstruction.ERROR_MESSAGE + SocketInstruction.separator + currentPath + " does not exist");
				}
				return;
			}
		}

		uiCompareFrame.removeAll();

		monitorLogcat(false);

		new Thread(new Runnable() {

			@Override
			public void run() {

				for (String currentPath : monkey_runner_file_path) {

					initProperDir(currentPath);

					FileUtils.deletePicturesInDirectory(new File(dir_device_picture));

					//
					setLabelText("start running " + new File(currentPath).getName() + " on " + device + ", please wait.");
					uiCompareFrame.disableMenu();
					JDialog dialog = DialogBuilder.showProgressDialog(uiCompareFrame, "start running " + new File(currentPath).getName() + " on " + device + ", please wait");
					SystemUtils.exec(monkey_runner + " " + currentPath + " " + device + " " + dir_device_picture, null);
					dialog.setVisible(false);
					uiCompareFrame.enableMenu();

					FileUtils.deletePicturesInDirectory(new File(dir_device_test_picture));
					FileUtils.copyFilesFromDirToDir(dir_device_picture, dir_device_test_picture);

					String[] testPictures = new File(dir_device_test_picture).list();
					ArrayList<String> testPictureList = new ArrayList<String>(Arrays.asList(testPictures));
					String[] targetPictures = new File(dir_device_target_picture).list();
					ArrayList<String> targetPictureList = new ArrayList<String>(Arrays.asList(targetPictures));
					for (int i = 0; i < testPictureList.size(); i++) {
						String fileName = testPictureList.get(i);
						if (!targetPictureList.contains(testPictureList.get(i))) {
							try {
								File fromFile = new File(dir_device_test_picture + File.separator + fileName);
								File toFile = new File(dir_device_target_picture + File.separator + fileName);
								FileUtils.copyFileFromFileToFile(fromFile, toFile);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					FileUtils.deletePicturesInDirectory(new File(dir_device_picture));
				}

				generateResult(true);

				monitorLogcat(true);
			}
		}).start();

	}

	private static void generateResult(boolean informFinish) {
		setLabelText("generating result...");

		String path = PropertyUtils.loadProperty(PropertyUtils.KEY_LAST_SCRIPT, PropertyUtils.NULL);
		uiCompareFrame.setScriptsName(path);
		if (path.equalsIgnoreCase(PropertyUtils.NULL)) {
			return;
		}

		String[] monkey_runner_file_path = path.split(",");

		for (String currentPath : monkey_runner_file_path) {
			if (currentPath == null || !new File(currentPath).exists()) {
				DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_file_disappear);
				if (socketServer != null && socketServer.isConnected()) {
					socketServer.write(SocketInstruction.ERROR_MESSAGE + SocketInstruction.separator + currentPath + " does not exist");
				}
				return;
			}
		}

		uiCompareFrame.removeAll();

		for (String currentPath : monkey_runner_file_path) {
			setLabelText("generating result for " + currentPath);

			initProperDir(currentPath);

			File resultDir = new File(dir_device_result_picture);
			if (!resultDir.exists())
				resultDir.mkdir();
			else
				FileUtils.deletePicturesInDirectory(resultDir);

			File targetDir = new File(dir_device_target_picture);
			File testDir = new File(dir_device_test_picture);

			boolean hasResult = false;
			int imageWidth = 360;
			int imageHeight = 640;
			for (String fileName : targetDir.list()) {
				for (String fileName2 : testDir.list()) {
					if (!fileName.equalsIgnoreCase(fileName2))
						continue;
					hasResult = true;

					String path1 = targetDir.getAbsolutePath() + File.separator + fileName;
					String path2 = testDir.getAbsolutePath() + File.separator + fileName;
					String path3 = resultDir.getAbsolutePath() + File.separator + fileName;

					try {
						BufferedImage targetBuffered = ImageIO.read(new File(path1));
						BufferedImage testBuffered = ImageIO.read(new File(path2));
						BufferedImage resultBuffered = ImageIO.read(new File(path1));
						File resultFile = new File(path3);

						Color red = new Color(255, 0, 0);

						for (int i = 0; i < targetBuffered.getWidth(); i++) {
							for (int j = 0; j < targetBuffered.getHeight(); j++) {
								if (targetBuffered.getRGB(i, j) != testBuffered.getRGB(i, j)) {
									resultBuffered.setRGB(i, j, red.getRGB());
								}
								else {
									Color color = new Color(testBuffered.getRGB(i, j));
									resultBuffered.setRGB(i, j, color.darker().darker().getRGB());
								}

							}
						}

						imageWidth = targetBuffered.getWidth();
						imageHeight = targetBuffered.getHeight();

						ImageIO.write(resultBuffered, "png", resultFile);

						targetBuffered.flush();
						targetBuffered = null;
						testBuffered.flush();
						testBuffered = null;
						resultBuffered.flush();
						resultBuffered = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (hasResult) {
				try {
					HtmlGenerator.createHtml(imageWidth / 3, imageHeight / 3);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				uiCompareFrame.showComparPictures(targetDir, testDir, resultDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		setLabelText("complete!");

		if (informFinish) {
			String username = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL, PropertyUtils.NULL);
			String password = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, PropertyUtils.NULL);
			if (username.equalsIgnoreCase(PropertyUtils.NULL) || password.equalsIgnoreCase(PropertyUtils.NULL)) {
				return;
			}
			String subject = "[no-reply] Notification From GogoMonkeyRun";
			EmailUtils.send(username, password, username, subject, "GogoMonkey Auto-Testing Complete!");
		}

		if (socketServer != null && socketServer.isConnected()) {
			socketServer.write(SocketInstruction.COMPLETE_RUNNING_SCRIPT);
		}
	}

	//
	private static boolean initProperDir(String monkey_runner_file_path) {
		//
		String deviceName = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		uiCompareFrame.setDeviceName(deviceName);
		if (deviceName.equalsIgnoreCase(PropertyUtils.NULL)) {
			DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_choose_a_device);
			setDefaultDevice(true);

			return false;
		}

		dir_device = "%s";
		dir_device = new File(String.format(dir_device, deviceName)).getAbsolutePath();
		if (!new File(dir_device).exists()) {
			new File(dir_device).mkdirs();
		}

		//
		dir_device_script = "%s/script";
		dir_device_script = new File(String.format(dir_device_script, deviceName)).getAbsolutePath();
		if (!new File(dir_device_script).exists()) {
			new File(dir_device_script).mkdirs();
		}

		if (monkey_runner_file_path == null || !new File(monkey_runner_file_path).exists()) {
			return false;
		}
		//
		dir_device_picture = "%s/%s/sreenshot";
		dir_device_target_picture = "%s/%s/sreenshot/target";
		dir_device_test_picture = "%s/%s/sreenshot/test";
		dir_device_result_picture = "%s/%s/sreenshot/result";
		String scriptName = new File(monkey_runner_file_path).getName().replace(".mr", "").replace(".py", "");

		dir_device_picture = new File(String.format(dir_device_picture, deviceName, scriptName)).getAbsolutePath();
		if (!new File(dir_device_picture).exists())
			new File(dir_device_picture).mkdirs();
		dir_device_target_picture = new File(String.format(dir_device_target_picture, deviceName, scriptName)).getAbsolutePath();
		if (!new File(dir_device_target_picture).exists())
			new File(dir_device_target_picture).mkdirs();
		dir_device_test_picture = new File(String.format(dir_device_test_picture, deviceName, scriptName)).getAbsolutePath();
		if (!new File(dir_device_test_picture).exists())
			new File(dir_device_test_picture).mkdirs();
		dir_device_result_picture = new File(String.format(dir_device_result_picture, deviceName, scriptName)).getAbsolutePath();
		if (!new File(dir_device_result_picture).exists())
			new File(dir_device_result_picture).mkdirs();

		return true;
	}

	private static void showQuikActionSelectDialog(boolean checkFile, String message) {
		//
		if (checkFile) {
			String lastScriptPath = PropertyUtils.loadProperty(PropertyUtils.KEY_LAST_SCRIPT, PropertyUtils.NULL);
			uiCompareFrame.setScriptsName(lastScriptPath);
			//
			if (lastScriptPath.equalsIgnoreCase(PropertyUtils.NULL)) {
				int re = DialogBuilder.showConfirmDialog(uiCompareFrame, R.string.dialog_alert_create_a_new_script);
				if (re == 0) {
					recordNewAction();
				}
				return;
			}
		}

		//
		int select = DialogBuilder.showConfirmDialog(uiCompareFrame, R.string.dialog_title_quikly_choose, message);
		if (select == 0) {
			startMonkeyRunner();
		}
	}

	private static void selectAPKtoInstall() {
		//
		apkName = DialogBuilder.showFindApkFileDialog(uiCompareFrame, getAllApks());
		installApk();
	}

	private static void installApk() {
		final JDialog dialog = DialogBuilder.showProgressDialog(uiCompareFrame, "Installing apk: " + apkName);
		System.out.println("install apk: " + apkName);
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (apkName == null) {
					if (socketServer != null && socketServer.isConnected()) {
						socketServer.write(SocketInstruction.ERROR_MESSAGE + SocketInstruction.separator + "apk does not exist");
					}
					DialogBuilder.showMessageDialog(uiCompareFrame, "Installing Fail for No Apk Found.");
					dialog.setVisible(false);
					return;
				}
				File apkDir = new File("apks");
				apkName = apkDir.getAbsolutePath() + File.separator + apkName;
				if (!new File(apkName).exists()) {
					if (socketServer != null && socketServer.isConnected()) {
						socketServer.write(SocketInstruction.ERROR_MESSAGE + SocketInstruction.separator + "apk does not exist");
					}
					DialogBuilder.showMessageDialog(uiCompareFrame, "Installing Fail for No Apk Found.");
					dialog.setVisible(false);
					return;
				}

				String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
				if (device.equalsIgnoreCase(PropertyUtils.NULL)) {
					dialog.setVisible(false);
					DialogBuilder.showMessageDialog(uiCompareFrame, "Installing Fail for No Devices Found.");
					return;
				}
				SystemUtils.exec(monkey_runner + " " + monkey_installer_file_path + " " + device + " " + apkName, null);

				apkName = null;

				if (socketServer != null && socketServer.isConnected()) {
					socketServer.write(SocketInstruction.COMPLETE_INSTALLING_APK);
				}
				dialog.setVisible(false);
				DialogBuilder.showMessageDialog(uiCompareFrame, "Installing Complete");
			}
		}).start();
	}

	private static String[] getAllApks() {
		File apkDir = new File("apks");
		if (!apkDir.exists())
			apkDir.mkdir();

		String[] fileNames = apkDir.list();
		return fileNames;
	}

	public static void inputPackageName() {
		String input = JOptionPane.showInputDialog(R.string.dialog_alert_input_package_name, package_name.equalsIgnoreCase("gogolook.callgogolook2") ? "(Default)" : package_name);
		if (input == null || input.equalsIgnoreCase("")) {
			return;
		}
		if (input.equalsIgnoreCase("(Default)")) {
			package_name = "gogolook.callgogolook2";
		}
		else {
			package_name = input;
		}

		final ArrayList<String> packaeNames = new ArrayList<String>();
		SystemUtils.exec(UICompareRunner.adb + " shell pm list packages", new OnExecCallBack() {

			@Override
			public void onExec(Process process, String line) {

			}

			@Override
			public void afterExec(String response, String error) {
				String[] packages = response.split("\n");
				packaeNames.addAll(new ArrayList<String>(Arrays.asList(packages)));

				if (!packaeNames.contains("package:" + package_name)) {
					DialogBuilder.showMessageDialog(uiCompareFrame, R.string.dialog_alert_package_name_error);
					package_name = "gogolook.callgogolook2";
				}
			}
		});
	}

	public static void setLabelText(String text) {
		uiCompareFrame.setLabelText(text);
	}

	private static void randomTest() {

		int count = 500;
		String input = JOptionPane.showInputDialog(R.string.dialog_alert_input_random_test_count);
		if (input == null) {
			return;
		}
		else {
			try {
				count = Integer.parseInt(input);
			} catch (java.lang.NumberFormatException e) {
				return;
			}
		}

		monitorLogcat(false);

		final String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		SystemUtils.exec(adb + " -s " + device + " shell monkey -p " + package_name + " -v " + count, new OnExecCallBack() {

			@Override
			public void onExec(Process process, String line) {

			}

			@Override
			public void afterExec(String response, String error) {
				monitorLogcat(true);
			}
		});

	}

	private static void setEmailForCrashReport(boolean reset) {
		if (reset) {
			PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL, PropertyUtils.NULL);
			PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, PropertyUtils.NULL);
		}
		else {
			String username = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL, PropertyUtils.NULL);
			String password = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, PropertyUtils.NULL);
			if (username.equalsIgnoreCase(PropertyUtils.NULL) || password.equalsIgnoreCase(PropertyUtils.NULL)) {
				int opt = DialogBuilder.showConfirmDialog(uiCompareFrame, R.string.dialog_alert_confirm_set_crash_report);
				if (opt == 0) {
					setEmailForCrashReport(true);
				}
			}
			return;
		}

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(R.string.dialog_alert_input_your_email));
		JTextField emailEdit = new JTextField();
		panel.add(emailEdit);
		panel.add(new JLabel(R.string.dialog_alert_input_your_email_password));
		JPasswordField passwordEdit = new JPasswordField();
		panel.add(passwordEdit);

		int result = JOptionPane.showConfirmDialog(null, panel, R.string.dialog_title_set_crash_report, JOptionPane.OK_CANCEL_OPTION);
		if (result == 0) {
			String defaultUsername = emailEdit.getText();
			if (defaultUsername == null) {
				PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL, PropertyUtils.NULL);
			}
			else {
				PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL, defaultUsername);
			}

			String defaultPassword = passwordEdit.getText();
			if (defaultPassword == null) {
				PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, PropertyUtils.NULL);
			}
			else {
				PropertyUtils.saveProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, defaultPassword);
			}
		}
	}

	private static Thread logcatThread;
	private static String errorLog = null;

	private static void monitorLogcat(boolean cancel) {
		final String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
		SystemUtils.exec(adb + " -s " + device + " logcat -c", null);

		if (logcatThread != null && logcatThread.isAlive()) {
			logcatThread.interrupt();
			logcatThread = null;
		}
		if (cancel) {
			System.out.println("Finish Monitoring Logcat");
			return;
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String dateString = "============= " + sdf.format(date) + " =============";
		try {
			String path = new File(dir_device).getAbsolutePath();
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/log.txt", true)));
			out.println(dateString);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logcatThread = new Thread(new Runnable() {

			@Override
			public void run() {
				final String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
				SystemUtils.exec(adb + " -s " + device + " logcat System.err:W *:S", new OnExecCallBack() {

					@Override
					public void onExec(Process process, String line) {
						// TODO
						if (!line.contains("System.err"))
							return;
						try {
							String path = new File(dir_device).getAbsolutePath();
							PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path + "/log.txt", true)));
							out.println(line);
							out.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						if (line.contains("Caused by")) {
							if (errorLog == null)
								errorLog = dateString + "\n" + line;
						}
						if (errorLog != null && errorLog.contains("Caused by")) {
							errorLog = errorLog + "\n" + line;
						}
						if (errorLog != null && errorLog.split("\n").length > 5) {
							String username = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL, PropertyUtils.NULL);
							String password = PropertyUtils.loadProperty(PropertyUtils.KEY_FROM_EMAIL_PASSWORD, PropertyUtils.NULL);
							if (username.equalsIgnoreCase(PropertyUtils.NULL) || password.equalsIgnoreCase(PropertyUtils.NULL)) {
								errorLog = null;

								process.destroy();
								return;
							}

							String subject = "[no-reply] Crash Report From GogoMonkeyRun";
							EmailUtils.send(username, password, username, subject, errorLog);
							errorLog = null;

							process.destroy();
						}
					}

					@Override
					public void afterExec(String response, String error) {

					}
				});
			}
		});
		logcatThread.start();
	}

	private static void initSocket() {
		socketServer = new SocketServer(new SocketStatusListener() {

			@Override
			public void onStatusChanged() {
				if (socketServer == null)
					return;
				if (socketServer.isConnected()) {
					if (socketFrame == null) {
						socketFrame = new SocketFrame(uiCompareFrame, new OnWindowCloseListener() {

							@Override
							public void onWindowClosing(String... output) {
								screenshotFlag = false;
								socketServer.close();
								socketServer.waitForConnection();
							}
						});
					}
					socketFrame.setDevice(socketServer.getInetAddress());
					socketFrame.setVisible(true);

					String device = PropertyUtils.loadProperty(PropertyUtils.KEY_DEVICE, PropertyUtils.NULL);
					socketServer.write(SocketInstruction.SOCKET_CONNECTED + SocketInstruction.separator + device);

					startSyncScreenWithSocket();
				}
				else {
					if (socketFrame != null) {
						socketFrame.setVisible(false);
					}
				}
			}

			@Override
			public void onSocketException(Exception e) {
				DialogBuilder.showMessageDialog(uiCompareFrame, e.toString());
			}

			@Override
			public void onRead(String recieve) {

				if (recieve.startsWith(SocketInstruction.SOCKET_CLOSE)) {
					socketFrame.appendCmd("Close socket");
					socketServer.close();
					socketServer.waitForConnection();
				}
				else if (recieve.startsWith(SocketInstruction.GET_ALL_DEVICES)) {
					socketFrame.appendCmd("Get all devices");
					SystemUtils.exec(adb + " " + "devices", new OnExecCallBack() {

						@Override
						public void onExec(Process process, String line) {

						}

						@Override
						public void afterExec(String response, String error) {
							if (!error.equalsIgnoreCase("")) {
								DialogBuilder.showMessageDialog(uiCompareFrame, error);
								return;
							}
							socketServer.write(SocketInstruction.GET_ALL_DEVICES + SocketInstruction.separator + response);
						}
					});
				}
				else if (recieve.startsWith(SocketInstruction.SET_DEVICE)) {
					socketFrame.appendCmd("Set default device");
					String selectDevice = recieve.split(SocketInstruction.separator)[1];
					PropertyUtils.saveProperty(PropertyUtils.KEY_DEVICE, selectDevice);
					initProperDir(null);
				}
				else if (recieve.startsWith(SocketInstruction.GET_ALL_SCRIPTS)) {
					socketFrame.appendCmd("Get all scripts");
					File scriptDir = new File(dir_device_script);
					String scriptFiles = "null";
					for (String fileName : scriptDir.list()) {
						if (fileName.endsWith(".py")) {
							if (scriptFiles.equalsIgnoreCase("null")) {
								scriptFiles = fileName;
							}
							else {
								scriptFiles = scriptFiles + "," + fileName;
							}
						}
					}
					socketServer.write(SocketInstruction.GET_ALL_SCRIPTS + SocketInstruction.separator + scriptFiles);
				}
				else if (recieve.startsWith(SocketInstruction.SET_SCRIPT)) {
					socketFrame.appendCmd("Set script to run");
					String[] scripts = recieve.split(SocketInstruction.separator)[1].split(",");
					// dir_device_script
					String monkey_runner_file_path = null;
					for (String script : scripts) {
						if (monkey_runner_file_path == null) {
							monkey_runner_file_path = new File(dir_device_script + File.separator + script).getAbsolutePath();
						}
						else {
							monkey_runner_file_path = monkey_runner_file_path + "," + new File(dir_device_script + File.separator + script).getAbsolutePath();
						}
					}

					PropertyUtils.saveProperty(PropertyUtils.KEY_LAST_SCRIPT, monkey_runner_file_path);
					uiCompareFrame.setScriptsName(monkey_runner_file_path);
					startMonkeyRunner();
				}
				else if (recieve.startsWith(SocketInstruction.GET_ALL_APKS)) {
					String[] apks = getAllApks();
					String apkFiles = "null";
					for (String fileName : apks) {
						if (apkFiles.equalsIgnoreCase("null")) {
							apkFiles = fileName;
						}
						else {
							apkFiles = apkFiles + "," + fileName;
						}
					}
					socketServer.write(SocketInstruction.GET_ALL_APKS + SocketInstruction.separator + apkFiles);
				}
				else if (recieve.startsWith(SocketInstruction.SET_APK)) {
					apkName = recieve.split(SocketInstruction.separator)[1];
					installApk();
				}
			}
		}).waitForConnection();
	}

	private static Thread screenshotThread;
	private static boolean screenshotFlag = false;

	private static void startSyncScreenWithSocket() {
		if (screenshotThread != null && screenshotThread.isAlive()) {
			screenshotFlag = false;
			screenshotThread.interrupt();
			screenshotThread = null;
		}

		if (socketServer != null && socketServer.isConnected()) {
			screenshotFlag = true;
			screenshotThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (screenshotFlag) {
						BufferedImage bufferedImage = ScreenUtils.capture();
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						try {
							ImageIO.write(bufferedImage, "png", outputStream);
							byte imgBytes[] = outputStream.toByteArray();

							if (socketServer != null && socketServer.isConnected()) {
								socketServer.write(imgBytes, null);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						try {
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			screenshotThread.start();
		}
	}
}
