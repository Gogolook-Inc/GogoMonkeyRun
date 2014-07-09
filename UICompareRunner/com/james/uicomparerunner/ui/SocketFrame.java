
package com.james.uicomparerunner.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import com.james.uicomparerunner.ui.uiinterface.OnWindowCloseListener;

public class SocketFrame extends JFrame {

	private JTextPane devicePane;

	public SocketFrame(JFrame parentFrame, final OnWindowCloseListener onWindowCloseListener) {
		setTitle("Remote");

		setLayout(new BorderLayout());

		devicePane = new JTextPane();
		devicePane.setEditable(false);

		this.add(devicePane, BorderLayout.CENTER);

		pack();

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (onWindowCloseListener != null)
					onWindowCloseListener.onWindowClosing();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void setDevice(String device) {
		String content = "Device connect:\n" + device;
		devicePane.setText(content);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pack();
			}
		});
	}

	public void appendCmd(String cmd) {
		String content = devicePane.getText() + "\n" + cmd;
		devicePane.setText(content);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pack();
			}
		});
	}
}
