
package com.james.uicomparerunner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.james.uicomparerunner.UICompareRunner;
import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;
import com.james.uicomparerunner.ui.view.CompareView;
import com.james.uicomparerunner.ui.view.ImageView;
import com.james.uicomparerunner.utils.AndroidShell;
import com.james.uicomparerunner.utils.AndroidShell.OnExitCallBack;
import com.james.uicomparerunner.utils.AndroidShell.OnShellExecCallBack;
import com.james.uicomparerunner.utils.SystemUtils;

public class UiCompareFrame extends JFrame {
	private int monitorWidth;
	private int monitorHeight;

	private SharedPreferenceEditFrame sharedPreferenceEditFrame;

	private JMenu[] jMenu = {
		new JMenu(R.string.menu_file),
		new JMenu(R.string.menu_device),
		new JMenu(R.string.menu_edit),
		new JMenu(R.string.menu_help)
	};
	private JMenuItem[] fileMenu = {
		new JMenuItem(R.string.menu_file_generate_script, KeyEvent.VK_G),
		new JMenuItem(R.string.menu_file_edit_recorder, KeyEvent.VK_E),
		new JMenuItem(R.string.menu_file_choose_script, KeyEvent.VK_C),
		new JMenuItem(R.string.menu_file_run_last_script, KeyEvent.VK_R),
		new JMenuItem(R.string.menu_file_show_last_result, KeyEvent.VK_S),
		new JMenuItem(R.string.menu_file_clear),
		new JMenuItem(R.string.menu_file_close, KeyEvent.VK_Q)
	};
	private JMenuItem[] deviceMenu = {
		new JMenuItem(R.string.menu_device_reset_device),
		new JMenuItem(R.string.menu_device_reset_apk),
		new JMenuItem(R.string.menu_device_reset_package_name),
		new JMenuItem(R.string.menu_device_random_test),
		new JMenuItem(R.string.menu_device_report_error)
	};
	private JMenuItem[] editMenu = {
		new JMenuItem(R.string.menu_open_editor)
	};
	private JMenuItem[] helpMenu = {
		new JMenuItem(R.string.menu_help_tutorial),
		new JMenuItem(R.string.menu_help_about),
		new JMenuItem(R.string.menu_help_screen_shot)
	};
	private JMenuBar jMenuBar = new JMenuBar();

	private JScrollPane scrollpane;
	private Box contentPanel;
	private JLabel deviceLabel;
	public JButton changeDeviceButton;
	private JLabel scriptsLabel;
	private JTextPane consoleText;

	private OnReplaceClickListener mOnReplaceClickListener;

	public interface OnReplaceClickListener {
		public void onReplace(String origin, String target);
	}

	public UiCompareFrame(ActionListener actionListener) {
		super();

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		monitorWidth = gd.getDisplayMode().getWidth();
		monitorHeight = gd.getDisplayMode().getHeight();

		for (JMenuItem item : fileMenu) {
			if (SystemUtils.isMac() && item.getMnemonic() == KeyEvent.VK_Q)
				continue;

			jMenu[0].add(item);
			item.setAccelerator(KeyStroke.getKeyStroke(item.getMnemonic(),
					SystemUtils.isMac() ? Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() : Event.CTRL_MASK));
			item.addActionListener(actionListener);
		}
		for (JMenuItem item : deviceMenu) {
			jMenu[1].add(item);
			item.setAccelerator(KeyStroke.getKeyStroke(item.getMnemonic(),
					SystemUtils.isMac() ? Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() : Event.CTRL_MASK));
			item.addActionListener(actionListener);
		}
		for (JMenuItem item : editMenu) {
			jMenu[2].add(item);
			item.addActionListener(actionListener);
		}
		for (JMenuItem item : helpMenu) {
			jMenu[3].add(item);
			item.addActionListener(actionListener);
		}
		for (JMenu temp : jMenu) {
			jMenuBar.add(temp);
		}

		this.setJMenuBar(jMenuBar);
		//
		this.setTitle(R.string.app_name);
		// this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(monitorWidth * 1 / 10, 10, monitorWidth * 7 / 10, monitorHeight * 8 / 10);
		this.setLayout(new BorderLayout());

		contentPanel = Box.createVerticalBox();

		scrollpane = new JScrollPane(contentPanel);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scrollpane, BorderLayout.CENTER);
		scrollpane.setVisible(true);

		// TODO
		Box informationPanel = Box.createVerticalBox();
		informationPanel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Information"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		this.add(informationPanel, BorderLayout.WEST);

		JLabel deviceTitle = new JLabel("Current Device");
		deviceTitle.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.GRAY),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		informationPanel.add(deviceTitle);

		deviceLabel = new JLabel();
		informationPanel.add(deviceLabel);

		JLabel scriptsTitle = new JLabel("Current Scripts");
		scriptsTitle.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.GRAY),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		informationPanel.add(scriptsTitle);

		scriptsLabel = new JLabel();
		informationPanel.add(scriptsLabel);

		consoleText = new JTextPane();
		consoleText.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Console"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		this.add(consoleText, BorderLayout.SOUTH);
		consoleText.setEditable(false);

		this.setVisible(true);
	}

	public void showComparPictures(File targetDir, File testDir, File resultDir) throws IOException {
		//
		setTitle(resultDir.getAbsolutePath());
		//
		for (String fileName : targetDir.list()) {
			for (String fileName2 : testDir.list()) {
				if (!fileName.equalsIgnoreCase(fileName2))
					continue;

				String path1 = targetDir.getAbsolutePath() + File.separator + fileName;
				ImageView view = new ImageView(new File(path1));

				String path2 = testDir.getAbsolutePath() + File.separator + fileName;
				ImageView view2 = new ImageView(new File(path2));

				String path3 = resultDir.getAbsolutePath() + File.separator + fileName;
				ImageView view3 = new ImageView(new File(path3));

				CompareView compareView = new CompareView(fileName, view, view2, view3);
				contentPanel.add(compareView);
				compareView.setVisible(true);

				addClickListener(view, path1, path1);
				addClickListener(view2, path2, path1);

				JPanel offsetPanel = new JPanel();
				contentPanel.add(offsetPanel);
				offsetPanel.setBounds(0, 0, 150, compareView.getHeight());
				offsetPanel.setVisible(true);
			}
		}

		refresh();
	}

	public void setOnReplaceClickListener(OnReplaceClickListener onReplaceClickListener) {
		mOnReplaceClickListener = onReplaceClickListener;
	}

	private void addClickListener(final ImageView view, final String origin, final String target) {

		view.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (mOnReplaceClickListener != null)
					mOnReplaceClickListener.onReplace(origin, target);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}

	public void removeAll() {
		this.setTitle("UI Comparison");

		for (Component component : contentPanel.getComponents()) {
			if (component instanceof CompareView)
				((CompareView) component).clear();
		}
		contentPanel.removeAll();

		SwingUtilities.updateComponentTreeUI(contentPanel);
		contentPanel.invalidate();
		contentPanel.validate();
		contentPanel.repaint();
	}

	public void checkSharedPreference(final String packageName) {
		final AndroidShell shell = new AndroidShell(UICompareRunner.adb);
		try {
			shell.start();

			shell.exec("run-as " + packageName);

			shell.exec("test -e " + "shared_prefs/" + packageName + "_preferences.xml && echo \"exist\" || echo \"Not exist\"", new OnShellExecCallBack() {

				@Override
				public void onExec(String response) {
					if (response.trim().equalsIgnoreCase("exist")) {
						editSharedPreference(shell, packageName);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void editSharedPreference(final AndroidShell shell, final String packageName) {
		try {
			shell.exec("chmod 666 shared_prefs/" + packageName + "_preferences.xml");
			shell.exec("exit");
			shell.stop(new OnExitCallBack() {

				@Override
				public void onExit() {
					try {
						handle();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				private void handle() throws IOException {
					String targetFile = "/data/data/" + packageName + "/shared_prefs/" + packageName + "_preferences.xml";
					String tmpDir = new File(UICompareRunner.dir_device).getAbsolutePath();
					SystemUtils.exec(UICompareRunner.adb + " pull " + targetFile + " " + tmpDir, null);

					File file = new File(tmpDir + File.separator + packageName + "_preferences.xml");
					showSharedPreferenceEditor(shell, file, tmpDir, packageName);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showSharedPreferenceEditor(final AndroidShell shell, final File xmlFile, final String tmpDir, final String packageName) {
		sharedPreferenceEditFrame = new SharedPreferenceEditFrame(this, xmlFile, new OnWindowCloseListener() {

			@Override
			public void onWindowClosing(String... output) {
				String tmpFile = tmpDir + File.separator + packageName + "_preferences.xml";
				String targetFile = "/data/data/" + packageName + "/shared_prefs/" + packageName + "_preferences.xml";

				SystemUtils.exec(UICompareRunner.adb + " shell rm " + targetFile, null);
				SystemUtils.exec(UICompareRunner.adb + " push " + tmpFile + " " + targetFile, null);

				try {
					shell.start();
					shell.exec("run-as " + packageName);
					shell.exec("chmod 660 shared_prefs/" + packageName + "_preferences.xml");
					shell.exec("exit");
					shell.stop(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setLabelText(String text) {
		String[] consoles = consoleText.getText().split("\n");

		int length = consoles.length;
		if (length >= 3) {
			consoleText.setText(consoles[length - 3] + "\n" + consoles[length - 2] + "\n" + consoles[length - 1] + "\n" + text);
		}
		else if (length >= 2) {
			consoleText.setText(consoles[length - 2] + "\n" + consoles[length - 1] + "\n" + text);
		}
		else if (length >= 1) {
			consoleText.setText(consoles[length - 1] + "\n" + text);
		}
		else {
			consoleText.setText(text);
		}

		consoleText.repaint();
		//		refresh();
	}

	public void setDeviceName(String deviceName) {
		deviceLabel.setText(deviceName);
		refresh();
	}

	public void setScriptsName(String scriptList) {
		String newScriptList = null;
		String[] scripts = scriptList.split(",");
		for (int i = 0; i < scripts.length; i++) {
			if (newScriptList == null) {
				newScriptList = new File(scripts[i]).getName();
			}
			else {
				newScriptList = newScriptList + "\n" + new File(scripts[i]).getName();
			}
		}
		scriptsLabel.setText(newScriptList);

		refresh();
	}

	public boolean isEditorShown() {
		if (sharedPreferenceEditFrame == null)
			return false;

		return sharedPreferenceEditFrame.isShowing();
	}

	private void refresh() {
		this.setBounds(getX(), getY(), getWidth(), getHeight() + 1);
		this.setBounds(getX(), getY(), getWidth(), getHeight() - 1);
		this.repaint();
	}
}
