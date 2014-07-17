
package com.android.monkeyrunner.recorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.recorder.actions.Action;
import com.android.monkeyrunner.recorder.actions.AdvancePressAction;
import com.android.monkeyrunner.recorder.actions.DragAction;
import com.android.monkeyrunner.recorder.actions.DragAction.Direction;
import com.android.monkeyrunner.recorder.actions.SnapshotAction;
import com.android.monkeyrunner.recorder.actions.TouchAction;
import com.android.monkeyrunner.recorder.actions.TypeAction;
import com.android.monkeyrunner.recorder.actions.WaitAction;
import com.android.monkeyrunner.res.R;
import com.android.monkeyrunner.utils.SystemUtils;

/**
 * MainFrame for MonkeyRecorder.
 */
public class MonkeyRecorderFrameExt extends JFrame implements ActionListener {
	private static final Logger LOG =
			Logger.getLogger(MonkeyRecorderFrameExt.class.getName());
	private final IChimpDevice device;
	private final String defaultExportDir;
	private static final long serialVersionUID = 1L;

	private static int scaleSize = 480;

	private static final int PORTRAIT = 0x1001;
	private static final int LANDSCAPE = 0x1002;
	private int orientation = PORTRAIT;

	private JMenu[] jMenu = {
		new JMenu(R.string.menu_file)
	};
	private JMenuItem[] fileMenu = {
		new JMenuItem(R.string.menu_file_export, KeyEvent.VK_S)
	};
	private JMenuBar jMenuBar = new JMenuBar();

	private JPanel jContentPane = null;
	private JLabel display = null;
	private JScrollPane historyPanel = null;
	private Box actionPanel = null;
	private JButton waitButton = null;
	private JButton pressButton = null;
	private JButton typeButton = null;
	private JButton exportActionButton = null;
	private JButton snapshotActionButton = null;
	private JButton resetActionButton = null;
	private JButton rotateButton = null;
	private JLabel textLabel = null;
	private BufferedImage currentImage; // @jve:decl-index=0:
	private BufferedImage scaledImage = new BufferedImage(scaleSize * 3 / 4, scaleSize,
			BufferedImage.TYPE_INT_ARGB); // @jve:decl-index=0:
	private JList historyList;
	private ActionListModel actionListModel;
	private ArrayList<Action> actions = new ArrayList<Action>();
	private final Timer refreshTimer = new Timer(200, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			refreshDisplay(); // @jve:decl-index=0:
		}
	});

	private boolean isFling = false;
	private Point flingStartPoint = new Point();
	private Point flingEndPoint = new Point();

	/**
	 * This is the default constructor
	 */
	public MonkeyRecorderFrameExt(IChimpDevice device, String defaultExportDir) {
		this.device = device;
		this.defaultExportDir = defaultExportDir;
		initialize();
	}

	private void initialize() {
		this.setLocation(150, 50);
		this.setSize(400, 600);
		this.setContentPane(getJContentPane());
		this.setTitle("MonkeyRecorder");

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

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refreshDisplay();
			}
		});
		refreshTimer.start();
	}

	private void rotateDisplay() {
		if (orientation == PORTRAIT) {
			orientation = LANDSCAPE;
		}
		else if (orientation == LANDSCAPE) {
			orientation = PORTRAIT;
		}
		refreshDisplay();
	}

	private void refreshDisplay() {
		if (orientation == PORTRAIT) {
			IChimpImage snapshot = device.takeSnapshot();
			currentImage = snapshot.createBufferedImage();

			int width = currentImage.getWidth();
			int height = currentImage.getHeight();
			int scaleWidth = width * scaleSize / height;
			int scaleHeight = scaleSize;
			display.setPreferredSize(new Dimension(scaleWidth, scaleHeight));
			if (scaledImage.getWidth() != scaleWidth || scaledImage.getHeight() != scaleHeight) {
				scaledImage = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
			}

			Graphics2D g = scaledImage.createGraphics();
			g.drawImage(currentImage, 0, 0,
					scaledImage.getWidth(), scaledImage.getHeight(),
					null);
			g.dispose();
			display.setIcon(new ImageIcon(scaledImage));
		}
		else if (orientation == LANDSCAPE) {
			IChimpImage snapshot = device.takeSnapshot();
			BufferedImage originImage = snapshot.createBufferedImage();

			int width = originImage.getHeight();
			int height = originImage.getWidth();
			int scaleWidth = scaleSize;
			int scaleHeight = height * scaleSize / width;
			display.setPreferredSize(new Dimension(scaleWidth, scaleHeight));
			if (scaledImage.getWidth() != scaleWidth || scaledImage.getHeight() != scaleHeight) {
				scaledImage = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
			}
			if (currentImage.getWidth() != width || currentImage.getHeight() != height) {
				currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}

			Graphics2D g2d = currentImage.createGraphics();
			AffineTransform transform1 = new AffineTransform();
			transform1.translate((currentImage.getWidth() - originImage.getWidth()) / 2, (currentImage.getHeight() - originImage.getHeight()) / 2);
			transform1.rotate(Math.toRadians(-90), originImage.getWidth() / 2, originImage.getHeight() / 2);
			g2d.setTransform(transform1);
			g2d.drawImage(originImage, 0, 0, null);
			g2d.dispose();

			Graphics2D g2d2 = scaledImage.createGraphics();
			g2d2.drawImage(currentImage, 0, 0,
					scaledImage.getWidth(), scaledImage.getHeight(),
					null);
			g2d2.dispose();
			display.setIcon(new ImageIcon(scaledImage));
		}
		pack();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDisplayPanel(), BorderLayout.CENTER);
			jContentPane.add(getHistoryPanel(), BorderLayout.EAST);
			jContentPane.add(getActionPanel(), BorderLayout.NORTH);

		}
		return jContentPane;
	}

	private JPanel getDisplayPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		//
		display = new JLabel();
		panel.add(display, BorderLayout.CENTER);

		display.setPreferredSize(new Dimension(scaleSize * 3 / 4, scaleSize));
		display.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				touch(event);
			}

			@Override
			public void mouseReleased(MouseEvent event) {
				if (isFling) {
					double scalex = ((double) currentImage.getWidth()) / ((double) scaledImage.getWidth());
					double scaley = ((double) currentImage.getHeight()) / ((double) scaledImage.getHeight());
					int x = (int) (event.getX() * scalex);
					int y = (int) (event.getY() * scaley);

					flingEndPoint.x = x;
					flingEndPoint.y = y;

					DragAction dragAction = newDragAction(flingStartPoint, flingEndPoint, 300);
					if (dragAction != null)
						addAction(dragAction);
				}
				isFling = false;
			}

			@Override
			public void mousePressed(MouseEvent event) {
				double scalex = ((double) currentImage.getWidth()) / ((double) scaledImage.getWidth());
				double scaley = ((double) currentImage.getHeight()) / ((double) scaledImage.getHeight());
				int x = (int) (event.getX() * scalex);
				int y = (int) (event.getY() * scaley);

				flingStartPoint.x = x;
				flingStartPoint.y = y;
			}
		});

		display.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent event) {
				detectMove(event);
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				isFling = true;
			}
		});

		return panel;
	}

	/**
	 * This method initializes historyPanel
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getHistoryPanel() {
		if (historyPanel == null) {
			historyPanel = new JScrollPane();
			historyPanel.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder("Action History"),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			historyPanel.getViewport().setView(getHistoryList());
		}
		return historyPanel;
	}

	private JList getHistoryList() {
		if (historyList == null) {
			actionListModel = new ActionListModel();
			historyList = new JList(actionListModel);
		}
		return historyList;
	}

	/**
	 * This method initializes actionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private Box getActionPanel() {
		if (actionPanel == null) {

			actionPanel = Box.createVerticalBox();

			Box horizontalBox1 = Box.createHorizontalBox();
			actionPanel.add(horizontalBox1);
			horizontalBox1.add(getExportActionButton());
			horizontalBox1.add(getPressButton());
			horizontalBox1.add(getTypeButton());

			Box horizontalBox2 = Box.createHorizontalBox();
			actionPanel.add(horizontalBox2);
			horizontalBox2.add(getWaitButton());
			horizontalBox2.add(getSnapshotActionButton());
			horizontalBox2.add(getRemoveActionButton());
			horizontalBox2.add(getRotateButton());
			// horizontalBox2.add(getRefreshButton());

			actionPanel.add(getTextLabel());
		}
		return actionPanel;
	}

	/**
	 * This method initializes waitButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getWaitButton() {
		if (waitButton == null) {
			waitButton = new JButton();
			waitButton.setText(R.string.button_wait);
			waitButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String howLongStr = JOptionPane.showInputDialog(R.string.text_how_many_second_to_wait);
					if (howLongStr != null) {
						float howLong = Float.parseFloat(howLongStr);
						addAction(new WaitAction(howLong));
					}
				}
			});
		}
		return waitButton;
	}

	/**
	 * This method initializes pressButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPressButton() {
		if (pressButton == null) {
			pressButton = new JButton();
			pressButton.setText(R.string.button_press_a_button);
			pressButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JPanel panel = new JPanel();
					JLabel text = new JLabel(R.string.text_what_button);
					JComboBox keys = new JComboBox(AdvancePressAction.KEYS);
					keys.setEditable(true);
					JComboBox direction = new JComboBox(AdvancePressAction.DOWNUP_FLAG_MAP.values().toArray());
					panel.add(text);
					panel.add(keys);
					panel.add(direction);
					int result = JOptionPane.showConfirmDialog(null, panel, "Input", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						// Look up the "flag" value for the press choice
						Map<String, String> lookupMap = AdvancePressAction.DOWNUP_FLAG_MAP.inverse();
						String flag = lookupMap.get(direction.getSelectedItem());
						addAction(new AdvancePressAction((String) keys.getSelectedItem(), flag));
					}
				}
			});
		}
		return pressButton;
	}

	/**
	 * This method initializes typeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getTypeButton() {
		if (typeButton == null) {
			typeButton = new JButton();
			typeButton.setText(R.string.button_type_something);
			typeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String whatToType = JOptionPane.showInputDialog(R.string.text_what_to_type);
					if (whatToType != null) {
						addAction(new TypeAction(whatToType));
					}
				}
			});
		}
		return typeButton;
	}

	private DragAction newDragAction(Point startPoint, Point endPoint, long millis) {
		// TODO
		int width = Integer.parseInt(device.getProperty(orientation == PORTRAIT ? "display.width" : "display.height"));
		int height = Integer.parseInt(device.getProperty(orientation == PORTRAIT ? "display.height" : "display.width"));

		Direction dir = Direction.NORTH;
		if (Math.abs(startPoint.x - endPoint.x) > width / 5) {
			if (startPoint.x >= endPoint.x) {
				dir = Direction.WEST;
			}
			else if (startPoint.x < endPoint.x) {
				dir = Direction.EAST;
			}
		}
		else if (Math.abs(startPoint.y - endPoint.y) > height / 5) {
			if (startPoint.y >= endPoint.y) {
				dir = Direction.NORTH;
			}
			else if (startPoint.y < endPoint.y) {
				dir = Direction.SOUTH;
			}
		}
		else {
			return null;
		}

		return new DragAction(dir, startPoint.x, startPoint.y, endPoint.x, endPoint.y, 1, millis);
	}

	/**
	 * This method initializes exportActionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportActionButton() {
		if (exportActionButton == null) {
			exportActionButton = new JButton();
			exportActionButton.setText(R.string.button_file_export);
			exportActionButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent ev) {
					exportActionFile();
				}
			});
		}
		return exportActionButton;
	}

	/**
	 * This method initializes snapshotActionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSnapshotActionButton() {
		if (snapshotActionButton == null) {
			snapshotActionButton = new JButton();
			snapshotActionButton.setText(R.string.button_take_snapshot);
			snapshotActionButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent ev) {
					addAction(new SnapshotAction());
				}
			});
		}
		return snapshotActionButton;
	}

	/**
	 * This method initializes removeActionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveActionButton() {
		if (resetActionButton == null) {
			resetActionButton = new JButton();
			resetActionButton.setText(R.string.button_reset_last_action);
			resetActionButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent ev) {
					resetLastAction();
				}
			});
		}
		return resetActionButton;
	}

	/**
	 * This method initializes rotateButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRotateButton() {
		if (rotateButton == null) {
			rotateButton = new JButton();
			rotateButton.setText(R.string.button_rotate_display);
			rotateButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					rotateDisplay();
				}
			});
		}
		return rotateButton;
	}

	private JPanel getTextLabel() {
		JPanel panel = new JPanel();
		panel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Coordinates"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		panel.setLayout(new BorderLayout());
		if (textLabel == null) {
			textLabel = new JLabel("x: 0, y:0");
		}
		panel.add(textLabel, BorderLayout.WEST);
		return panel;
	}

	private void touch(MouseEvent event) {
		// TODO
		int x = event.getX();
		int y = event.getY();
		// Since we scaled the image down, our x/y are scaled as well.
		double scalex = ((double) currentImage.getWidth()) / ((double) scaledImage.getWidth());
		double scaley = ((double) currentImage.getHeight()) / ((double) scaledImage.getHeight());
		x = (int) (x * scalex);
		y = (int) (y * scaley);
		switch (event.getID()) {
			case MouseEvent.MOUSE_CLICKED:
				addAction(new TouchAction(x, y, MonkeyDevice.DOWN_AND_UP));
				break;
			case MouseEvent.MOUSE_PRESSED:
				addAction(new TouchAction(x, y, MonkeyDevice.DOWN));
				break;
			case MouseEvent.MOUSE_RELEASED:
				addAction(new TouchAction(x, y, MonkeyDevice.UP));
				break;
		}
	}

	private void detectMove(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		// Since we scaled the image down, our x/y are scaled as well.
		double scalex = ((double) currentImage.getWidth()) / ((double) scaledImage.getWidth());
		double scaley = ((double) currentImage.getHeight()) / ((double) scaledImage.getHeight());
		x = (int) (x * scalex);
		y = (int) (y * scaley);

		textLabel.setText("x: " + x + ", y: " + y);
	}

	public void addAction(Action a) {
		actions.add(a);
		actionListModel.add(a);
		scrollToBottom();
		try {
			if (!(a instanceof WaitAction)) {
				a.execute(device);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unable to execute action!", e);
		}
	}

	private void scrollToBottom() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int lastIndex = historyList.getModel().getSize() - 1;
				if (lastIndex >= 0) {
					historyList.ensureIndexIsVisible(lastIndex);
				}
			}
		});
	}

	private void resetLastAction() {
		if (actions.size() == 0)
			return;

		actions.remove(actions.size() - 1);
		ActionListModel newActionListModel = new ActionListModel();
		for (int i = 0; i < actions.size(); i++) {
			newActionListModel.add(actions.get(i));
		}

		actionListModel = newActionListModel;
		refreshHistoryPanel();
	}

	private void refreshHistoryPanel() {
		historyList = new JList(actionListModel);
		historyPanel.getViewport().setView(historyList);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			if (((JMenuItem) e.getSource()).getText().equalsIgnoreCase(R.string.menu_file_export)) {
				exportActionFile();
			}
		}
	}

	private void exportActionFile() {
		FileNameExtensionFilter mrFilter = new FileNameExtensionFilter(".mr", "mr");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(mrFilter);

		if (defaultExportDir != null) {
			fc.setCurrentDirectory(new File(defaultExportDir));
		}

		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String exportFileName = fc.getSelectedFile().getAbsolutePath();
				if (!exportFileName.endsWith(".mr")) {
					exportFileName = exportFileName + ".mr";
				}
				actionListModel.export(new File(exportFileName));

				JOptionPane.showMessageDialog(this, R.string.dialog_export_complete);
			} catch (FileNotFoundException e) {
				LOG.log(Level.SEVERE, "Unable to save file", e);
			}
		}
		//		if (SystemUtils.isMac()) {
		//			FileDialog mrChooser = new FileDialog(this, "Save", FileDialog.SAVE);
		//			if (defaultExportDir != null) {
		//				mrChooser.setDirectory(new File(defaultExportDir).getAbsolutePath());
		//			}
		//			mrChooser.setLocation(50, 50);
		//			mrChooser.setVisible(true);
		//			if (mrChooser.getFile() != null) {
		//				System.out.println("mrChooser.getFile(): " + mrChooser.getFile());
		//				try {
		//					String exportFileName = mrChooser.getFile();
		//					if (!exportFileName.endsWith(".mr")) {
		//						exportFileName = exportFileName + ".mr";
		//					}
		//					actionListModel.export(new File(exportFileName));
		//
		//					JOptionPane.showMessageDialog(this, R.string.dialog_export_complete);
		//				} catch (FileNotFoundException e) {
		//					LOG.log(Level.SEVERE, "Unable to save file", e);
		//				}
		//			}
		//		}
		//		else {
		//			FileNameExtensionFilter mrFilter = new FileNameExtensionFilter(".mr", "mr");
		//			JFileChooser fc = new JFileChooser();
		//			fc.setFileFilter(mrFilter);
		//
		//			if (defaultExportDir != null) {
		//				fc.setCurrentDirectory(new File(defaultExportDir));
		//			}
		//
		//			if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		//				try {
		//					String exportFileName = fc.getSelectedFile().getAbsolutePath();
		//					if (!exportFileName.endsWith(".mr")) {
		//						exportFileName = exportFileName + ".mr";
		//					}
		//					actionListModel.export(new File(exportFileName));
		//
		//					JOptionPane.showMessageDialog(this, R.string.dialog_export_complete);
		//				} catch (FileNotFoundException e) {
		//					LOG.log(Level.SEVERE, "Unable to save file", e);
		//				}
		//			}
		//		}
	}
}
