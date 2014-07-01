
package com.james.uicomparerunner.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.james.uicomparerunner.res.R;
import com.james.uicomparerunner.ui.dialog.DialogBuilder;
import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;

public class ScriptConcatenateFrame extends JFrame implements ActionListener {

	private JFrame parentFrame;
	private Box scriptBox;
	private JButton addButton;
	private JButton removeButton;
	private JButton clearButton;
	private JButton runButton;

	private ArrayList<String> scripts = new ArrayList<String>();

	private OnWindowCloseListener mOnWindowCloseListener;

	public ScriptConcatenateFrame(JFrame parentFrame, String defaultScriptFile, final OnWindowCloseListener onWindowCloseListener) {
		super();

		this.parentFrame = parentFrame;

		this.mOnWindowCloseListener = onWindowCloseListener;

		setLayout(new BorderLayout());

		setLocation(parentFrame.getX() + parentFrame.getWidth() / 10, parentFrame.getY() + parentFrame.getHeight() / 10);
		setSize(parentFrame.getWidth() * 8 / 10, parentFrame.getHeight() * 8 / 10);

		//Lay out the text controls and the labels.
		JPanel resultPane = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		resultPane.setLayout(borderLayout);
		resultPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Scripts List"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		scriptBox = Box.createVerticalBox();
		resultPane.add(scriptBox, BorderLayout.NORTH);

		//
		Box buttonBox = Box.createHorizontalBox();
		addButton = new JButton(R.string.button_add_script);
		addButton.addActionListener(this);
		removeButton = new JButton(R.string.button_remove_last_script);
		removeButton.addActionListener(this);
		clearButton = new JButton(R.string.button_clear_script);
		clearButton.addActionListener(this);
		runButton = new JButton(R.string.button_run_script);
		runButton.addActionListener(this);
		buttonBox.add(addButton);
		buttonBox.add(removeButton);
		buttonBox.add(clearButton);
		buttonBox.add(runButton);

		//Put everything together.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(resultPane, BorderLayout.CENTER);
		contentPane.add(buttonBox, BorderLayout.NORTH);

		add(contentPane);

		setVisible(true);

		if (defaultScriptFile != null) {
			addScript(defaultScriptFile);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addButton)) {
			String scriptFile = DialogBuilder.showFindScriptFileDialog(this);
			addScript(scriptFile);
		}
		else if (e.getSource().equals(removeButton)) {
			if (scripts.size() > 0) {
				scripts.remove(scripts.size() - 1);
				scriptBox.remove(scriptBox.getComponentCount() - 1);
			}
			scriptBox.repaint();
			scriptBox.invalidate();
		}
		else if (e.getSource().equals(clearButton)) {
			scripts.clear();
			scriptBox.removeAll();

			scriptBox.repaint();
			scriptBox.invalidate();
		}
		else if (e.getSource().equals(runButton)) {
			dispose();
			mOnWindowCloseListener.onWindowClosing(scripts.toArray(new String[scripts.size()]));
		}
	}

	private void addScript(String scriptFile) {
		scripts.add(scriptFile);
		JLabel label = new JLabel(new File(scriptFile).getName());
		scriptBox.add(label);

		setSize(parentFrame.getWidth() * 8 / 10 + 1, parentFrame.getHeight() * 8 / 10 + 1);
		setSize(parentFrame.getWidth() * 8 / 10, parentFrame.getHeight() * 8 / 10);
	}
}
