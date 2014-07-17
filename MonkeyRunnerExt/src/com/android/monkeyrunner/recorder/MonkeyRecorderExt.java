
package com.android.monkeyrunner.recorder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.WindowConstants;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.monkeyrunner.MonkeyDevice;

/**
 * Helper entry point for MonkeyRecorder.
 */
public class MonkeyRecorderExt {
	private static final Logger LOG = Logger.getLogger(MonkeyRecorderExt.class.getName());
	// This lock is used to keep the python process blocked while the frame is
	// runing.
	private static final Object LOCK = new Object();

	/**
	 * Jython entry point for MonkeyRecorder. Meant to be called like this:
	 * <code>
	 * from com.android.monkeyrunner import MonkeyRunner as mr
	 * from com.android.monkeyrunner import MonkeyRecorderExt
	 * MonkeyRecorderExt.start(mr.waitForConnection())
	 * </code>
	 * 
	 * @param device
	 */
	public static void start(final MonkeyDevice device) {
		start(device.getImpl(), null);
	}

	public static void start(final MonkeyDevice device, final String defaultExportPath) {
		start(device.getImpl(), defaultExportPath);
	}

	/* package */static void start(final IChimpDevice device, final String defaultExportPath) {
		MonkeyRecorderFrameExt frame = new MonkeyRecorderFrameExt(device, defaultExportPath);
		// TODO: this is a hack until the window listener works.
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				device.shell("stop");
				device.dispose();
				synchronized (LOCK) {
					LOCK.notifyAll();
				}
			}
		});
		frame.setVisible(true);
		synchronized (LOCK) {
			try {
				LOCK.wait();
			} catch (InterruptedException e) {
				LOG.log(Level.SEVERE, "Unexpected Exception", e);
			}
		}
	}

	public static void main(String[] args) {
		ChimpChat chimp = ChimpChat.getInstance();
		MonkeyRecorderExt.start(chimp.waitForConnection(), null);
	}
}
