
package com.james.uicomparerunner.ui.dialog;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.james.uicomparerunner.UICompareRunner;
import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.utils.PropertyUtils;

public class DialogBuilder {

	public static void showMessageDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog(parentComponent, message);
	}

	public static int showConfirmDialog(Component parentComponent, String message) {
		return JOptionPane.showConfirmDialog(parentComponent, message);
	}

	public static int showConfirmDialog(Component parentComponent, String title, String message) {
		return JOptionPane.showConfirmDialog(parentComponent, message, title, JOptionPane.OK_CANCEL_OPTION);
	}

	public static String showDeviceSelectDialog(Component parentComponent, Object[] possibilities) {
		if (possibilities == null || possibilities.length == 0) {
			JOptionPane.showMessageDialog(parentComponent, R.string.dialog_alert_no_devices);
			return null;
		}

		String select = (String) JOptionPane.showInputDialog(parentComponent,
				R.string.dialog_title_choose_a_device, R.string.dialog_title_choose_a_device, JOptionPane.PLAIN_MESSAGE,
				null, possibilities, possibilities[0].toString());

		return select;
	}

	public static void showSettingSDKPathDialog(Component parentComponent) {
		JOptionPane.showMessageDialog(parentComponent, R.string.dialog_alert_lack_of_sdk);

		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		dirChooser.setAcceptAllFileFilterUsed(false);
		// 決定檔案儲存路徑
		String fileDir = null;
		dirChooser.setDialogTitle(R.string.dialog_title_choose_sdk);
		if (dirChooser.showOpenDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
			fileDir = dirChooser.getSelectedFile().getAbsolutePath();
		}

		if (fileDir != null)
			PropertyUtils.saveProperty("sdk_path", fileDir);
	}

	public static String showFindActionFileDialog(Component parentComponent) {
		JOptionPane.showMessageDialog(parentComponent, R.string.dialog_alert_find_action_file);

		return showFindRecorderFileDialog(parentComponent);
	}

	public static String showFindRecorderFileDialog(Component parentComponent) {
		//
		FileNameExtensionFilter mrFilter = new FileNameExtensionFilter(".mr", "mr");
		JFileChooser mrChooser = new JFileChooser();
		mrChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		mrChooser.setCurrentDirectory(new File(UICompareRunner.dir_device));
		mrChooser.setFileFilter(mrFilter);

		int confirm = mrChooser.showOpenDialog(parentComponent);
		if (mrChooser.getSelectedFile() != null && confirm == 0) {
			String loadPath = mrChooser.getSelectedFile().getAbsolutePath();
			if (loadPath != null) {
				return loadPath;
			}
		}

		return null;
	}

	public static String showFindScriptFileDialog(Component parentComponent) {
		//
		File scriptDir = new File(UICompareRunner.dir_device_script);
		ArrayList<String> scriptFiles = new ArrayList<String>();
		for (String fileName : scriptDir.list()) {
			if (fileName.endsWith(".py")) {
				scriptFiles.add(fileName);
			}
		}

		String select = (String) JOptionPane
				.showInputDialog(parentComponent,
						R.string.dialog_title_script_select, R.string.dialog_title_script_select, JOptionPane.PLAIN_MESSAGE,
						null, scriptFiles.toArray(new String[scriptFiles.size()]),
						scriptFiles.toArray(new String[scriptFiles.size()]).length > 0 ? scriptFiles.toArray(new String[scriptFiles.size()])[0] : "");
		if (select != null) {
			String loadPath = new File(scriptDir.getAbsolutePath() + "/" + select).getAbsolutePath();
			return loadPath;
		}

		return null;
	}
}
