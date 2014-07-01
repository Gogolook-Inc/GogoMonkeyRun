
package com.james.uicomparerunner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.ui.dialog.DialogBuilder;
import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;
import com.james.uicomparerunner.utils.PropertyUtils;
import com.james.uicomparerunner.utils.ScriptGenerator;
import com.james.uicomparerunner.utils.SystemUtils;

public class RecorderEditFrame extends JFrame implements ActionListener {
	private int monitorWidth;
	private int monitorHeight;

	private JMenu[] jMenu = {
		new JMenu(R.string.menu_file)
	};
	private JMenuItem[] fileMenu = {
		new JMenuItem(R.string.menu_file_save, KeyEvent.VK_S),
		new JMenuItem(R.string.menu_file_close, KeyEvent.VK_Q)
	};
	private JMenuBar jMenuBar = new JMenuBar();

	private JScrollPane scrollpane;
	private Box contentBoxPanel;
	private JButton startButton;
	private JButton endButton;
	private JButton whileButton;
	private JButton saveButton;
	private JButton clearDataButton;
	private JTextArea recordText;
	private JTextArea scriptText;

	private String mrPath;

	private String comments;

	public RecorderEditFrame(final OnWindowCloseListener onWindowCloseListener) {
		super();

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		monitorWidth = gd.getDisplayMode().getWidth();
		monitorHeight = gd.getDisplayMode().getHeight();

		for (JMenuItem item : fileMenu) {
			jMenu[0].add(item);
			item.setAccelerator(KeyStroke.getKeyStroke(item.getMnemonic(),
					SystemUtils.isMac() ? Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() : Event.CTRL_MASK));
			item.addActionListener(this);
		}
		for (JMenu temp : jMenu) {
			jMenuBar.add(temp);
		}

		this.setJMenuBar(jMenuBar);

		this.setTitle(R.string.menu_file_edit_recorder);
		this.setVisible(true);
		this.setBounds(monitorWidth * 1 / 10, monitorHeight * 1 / 10, monitorWidth * 8 / 10, monitorHeight * 8 / 10);

		contentBoxPanel = Box.createHorizontalBox();

		scrollpane = new JScrollPane(contentBoxPanel);

		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setSize(getWidth() * 8 / 10 - 50, getHeight() * 9 / 10 - 50);

		this.add(scrollpane, BorderLayout.CENTER);
		scrollpane.setVisible(true);

		Box leftBox = Box.createVerticalBox();
		leftBox.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Edit Record"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		Box rightBox = Box.createVerticalBox();
		rightBox.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Script"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		Box buttonBox1 = Box.createHorizontalBox();
		Box buttonBox2 = Box.createHorizontalBox();

		saveButton = new JButton(R.string.button_save);
		saveButton.setVerticalTextPosition(AbstractButton.CENTER);
		saveButton.setHorizontalTextPosition(AbstractButton.LEADING);
		saveButton.addActionListener(this);
		saveButton.setToolTipText("Click this button to save & view script code.");
		buttonBox1.add(saveButton);

		startButton = new JButton(R.string.button_start_from_desktop);
		startButton.setVerticalTextPosition(AbstractButton.CENTER);
		startButton.setHorizontalTextPosition(AbstractButton.LEADING);
		startButton.addActionListener(this);
		startButton.setToolTipText("Click this button to start from desktop.");
		buttonBox1.add(startButton);

		endButton = new JButton(R.string.button_end_to_desktop);
		endButton.setVerticalTextPosition(AbstractButton.CENTER);
		endButton.setHorizontalTextPosition(AbstractButton.LEADING);
		endButton.addActionListener(this);
		endButton.setToolTipText("Click this button to back to desktop.");
		buttonBox1.add(endButton);

		whileButton = new JButton(R.string.button_add_while);
		whileButton.setVerticalTextPosition(AbstractButton.CENTER);
		whileButton.setHorizontalTextPosition(AbstractButton.LEADING);
		whileButton.addActionListener(this);
		whileButton.setEnabled(false);
		whileButton.setToolTipText("Click this button to add while loop.");
		buttonBox2.add(whileButton);

		clearDataButton = new JButton(R.string.button_clear_data);
		clearDataButton.setVerticalTextPosition(AbstractButton.CENTER);
		clearDataButton.setHorizontalTextPosition(AbstractButton.LEADING);
		clearDataButton.addActionListener(this);
		clearDataButton.setToolTipText("Click this button to clear all data.");
		buttonBox2.add(clearDataButton);

		recordText = new JTextArea();
		leftBox.add(buttonBox1);
		leftBox.add(buttonBox2);
		leftBox.add(recordText);

		scriptText = new JTextArea();
		scriptText.setBackground(Color.YELLOW);
		scriptText.setEditable(false);
		rightBox.add(scriptText);
		scriptText.setVisible(true);

		contentBoxPanel.add(leftBox);
		contentBoxPanel.add(rightBox);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				String scriptPath = PropertyUtils.loadProperty(PropertyUtils.KEY_LAST_SCRIPT, PropertyUtils.NULL);
				onWindowCloseListener.onWindowClosing(scriptPath.equalsIgnoreCase(PropertyUtils.NULL) ? null : scriptPath);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}

	public RecorderEditFrame setRecorder(String mrPath) throws IOException {
		this.mrPath = mrPath;
		this.setTitle(mrPath);

		recordText.setText("");

		//
		Reader rd = new FileReader(mrPath);
		BufferedReader reader = new BufferedReader(rd);

		String code = null;
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (code == null)
				code = line;
			else
				code = code + "\n" + line;
		}
		reader.close();

		recordText.setText(code);

		comments = "# " + new File(mrPath).getName() + "\n" +
				"# add " + ScriptGenerator.START_FROM_DESKTOP + " at the 'first' line of code\n" +
				"# to start app from desktop.\n" +
				"# add " + ScriptGenerator.END_BACK_TO_DESKTOP + " at the 'last' line of code.\n" +
				"# to end app back to desktop.";
		String originCode = getOriginCode();

		recordText.setText(comments + "\n" +
				originCode);

		recordText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				recordText.getSelectedText();
				if (recordText.getSelectedText() == null || recordText.getText().equalsIgnoreCase("")) {
					whileButton.setEnabled(false);
				}
				else {
					whileButton.setEnabled(true);
				}
			}
		});

		overwriteRecorder(false);
		updateScriptCode();

		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			if (((JMenuItem) e.getSource()).getText().equalsIgnoreCase(R.string.menu_file_save)) {
				try {
					overwriteRecorder(true);
					updateScriptCode();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				scriptText.setVisible(true);
			}
			else if (((JMenuItem) e.getSource()).getText().equalsIgnoreCase(R.string.menu_file_close)) {
				close();
			}
		}
		else if (e.getSource().equals(startButton)) {
			addStartCode();
		}
		else if (e.getSource().equals(endButton)) {
			addEndCode();
		}
		else if (e.getSource().equals(whileButton)) {
			addWhileLoopCode();
		}
		else if (e.getSource().equals(saveButton)) {
			try {
				overwriteRecorder(true);
				updateScriptCode();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			scriptText.setVisible(true);
		}
		else if (e.getSource().equals(clearDataButton)) {
			addClearDataCode();
		}
	}

	private void addStartCode() {
		String code = getOriginCode();
		if (code.contains(ScriptGenerator.START_FROM_DESKTOP))
			return;

		recordText.setText(comments + "\n" +
				ScriptGenerator.START_FROM_DESKTOP + "\n" +
				code);
	}

	private void addEndCode() {
		String code = getOriginCode();
		if (code.contains(ScriptGenerator.END_BACK_TO_DESKTOP))
			return;

		recordText.setText(comments + "\n" +
				code + "\n" +
				ScriptGenerator.END_BACK_TO_DESKTOP);
	}

	private void addClearDataCode() {
		String code = getOriginCode();
		if (code.contains(ScriptGenerator.CLEAR_DATA))
			return;

		recordText.setText(comments + "\n" +
				ScriptGenerator.START_FROM_DESKTOP + "\n" +
				ScriptGenerator.CLEAR_DATA + "\n" +
				code);
	}

	private void addWhileLoopCode() {
		String loopCount = JOptionPane.showInputDialog(null, "Enter loop count : ",
				"5", 1);
		if (!isNumeric(loopCount)) {
			DialogBuilder.showMessageDialog(this, R.string.dialog_alert_input_number);
			return;
		}

		String code = getOriginCode();
		String replacedCode = recordText.getSelectedText();
		String loop = "LOOP(" + loopCount + ")\n" + replacedCode + (replacedCode.endsWith("\n") ? "" : "\n") + "END LOOP" + (replacedCode.endsWith("\n") ? "\n" : "");

		code = code.replace(recordText.getSelectedText(), loop);

		recordText.setText(comments + "\n" +
				code);
	}

	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}

	private String getOriginCode() {
		String originalCode = null;
		String[] splits = recordText.getText().split("\n");
		for (String line : splits) {
			if (line.startsWith("#"))
				continue;
			String replaceLine = line;
			if (line.equalsIgnoreCase("START_FROM_DESKTOP")) {
				replaceLine = replaceLine.replace("START_FROM_DESKTOP", ScriptGenerator.START_FROM_DESKTOP);
			}
			if (line.equalsIgnoreCase("END_BACK_TO_DESKTOP")) {
				replaceLine = replaceLine.replace("END_BACK_TO_DESKTOP", ScriptGenerator.END_BACK_TO_DESKTOP);
			}
			if (originalCode == null) {
				originalCode = replaceLine;
			}
			else {
				originalCode = originalCode + "\n" + replaceLine;
			}
		}
		return originalCode;
	}

	private void overwriteRecorder(boolean showDialog) throws IOException {
		Writer wt = new FileWriter(new File(mrPath));
		BufferedWriter writer = new BufferedWriter(wt);

		String[] codeLine = recordText.getText().split("\n");
		for (int i = 0; i < codeLine.length; i++) {
			writer.write(codeLine[i]);
			writer.newLine();
		}

		writer.close();

		String monkey_runner_file_path = null;
		try {
			monkey_runner_file_path = ScriptGenerator.getScriptFilePath(mrPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyUtils.saveProperty(PropertyUtils.KEY_LAST_SCRIPT, monkey_runner_file_path);

		if (showDialog)
			DialogBuilder.showMessageDialog(this, R.string.dialog_alert_save_success);
	}

	public void updateScriptCode() throws IOException {
		String scriptPath = PropertyUtils.loadProperty(PropertyUtils.KEY_LAST_SCRIPT, PropertyUtils.NULL);
		if (!scriptPath.equalsIgnoreCase(PropertyUtils.NULL) && new File(scriptPath).exists()) {
			Reader rd = new FileReader(scriptPath);
			BufferedReader reader = new BufferedReader(rd);

			String code = null;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (code == null)
					code = line;
				else
					code = code + "\n" + line;
			}
			reader.close();

			scriptText.setText(code);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollpane.getViewport().setViewPosition(new java.awt.Point(0, 0));
			}
		});
	}

	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
