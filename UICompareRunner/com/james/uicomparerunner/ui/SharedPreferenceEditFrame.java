
package com.james.uicomparerunner.ui;

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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.ui.dialog.DialogBuilder;
import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;

public class SharedPreferenceEditFrame extends JFrame {

	private JTextArea textArea;

	public SharedPreferenceEditFrame(JFrame parentFrame, final File xmlFile, final OnWindowCloseListener onWindowCloseListener) {
		super();

		try {
			textArea = new JTextArea(loadSharedPreference(xmlFile));
			textArea.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder("Shared Preference"),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			this.setTitle(xmlFile.getAbsolutePath());
			this.add(textArea);
			this.setLocation(parentFrame.getX() + parentFrame.getWidth() / 10, parentFrame.getY() + parentFrame.getHeight() / 10);
			this.setSize(parentFrame.getWidth() * 8 / 10, parentFrame.getHeight() * 8 / 10);
			this.setVisible(true);
			this.addWindowListener(new WindowListener() {

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
					int confirm = DialogBuilder.showConfirmDialog(SharedPreferenceEditFrame.this, R.string.dialog_alert_save_before_leaving);
					if (confirm != 0)
						return;

					try {
						saveChange(xmlFile, textArea.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}

					onWindowCloseListener.onWindowClosing();
				}

				@Override
				public void windowClosed(WindowEvent arg0) {

				}

				@Override
				public void windowActivated(WindowEvent arg0) {

				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String loadSharedPreference(File xmlFile) throws IOException {
		Reader rd = new FileReader(xmlFile.getAbsolutePath());
		BufferedReader reader = new BufferedReader(rd);
		String line = null;
		String allText = null;
		while ((line = reader.readLine()) != null) {
			if (allText == null) {
				allText = line;
			}
			else {
				allText = allText + "\n" + line;
			}
		}
		reader.close();

		return allText;
	}

	private void saveChange(File xmlFile, String allText) throws IOException {
		Writer wr = new FileWriter(xmlFile.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(wr);
		String[] lines = allText.split("\n");
		for (int i = 0; i < lines.length; i++) {
			writer.write(lines[i]);
			writer.newLine();
		}
		writer.close();
	}
}
