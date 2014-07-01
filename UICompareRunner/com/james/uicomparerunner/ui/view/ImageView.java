
package com.james.uicomparerunner.ui.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageView extends JLabel {
	private static String TAG = ImageView.class.getSimpleName();

	// 記錄原來的尺寸
	private int originalWidth, originalHeight;

	/**
	 * @param objectId
	 * @param className
	 * @param path
	 * @param frameEditor
	 * @throws IOException
	 */
	public ImageView(File imgFile) throws IOException {
		this.setBackground(null);
		this.setOpaque(false);

		BufferedImage bi = ImageIO.read(imgFile);
		BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(bi, 0, 0, null);
		g.dispose();

		BufferedImage scaledImage = new BufferedImage(bi.getWidth() / 3, bi.getHeight() / 3,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaledImage.createGraphics();
		g2d.drawImage(bi, 0, 0,
				scaledImage.getWidth(), scaledImage.getHeight(),
				null);
		g2d.dispose();

		setIcon(new ImageIcon(scaledImage));
	}
}
