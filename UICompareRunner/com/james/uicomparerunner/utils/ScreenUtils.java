
package com.james.uicomparerunner.utils;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ScreenUtils {

	public static BufferedImage capture() {
		try {
			Robot robot = new Robot();

			// Capture the whole screen
			Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage bufferedImage = robot.createScreenCapture(area);

			BufferedImage scaledImage = new BufferedImage(bufferedImage.getWidth() / 20, bufferedImage.getHeight() / 20, BufferedImage.TYPE_INT_RGB);

			// Paint scaled version of image to new image
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(bufferedImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);

			// clean up
			graphics2D.dispose();

			return scaledImage;

		} catch (AWTException e) {
			System.out.println("Could not capture screen " + e.getMessage());
		}

		return null;
	}
}
