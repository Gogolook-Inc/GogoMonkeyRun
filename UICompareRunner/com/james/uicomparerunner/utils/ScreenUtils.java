
package com.james.uicomparerunner.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenUtils {

	public static void capture() {
		try {
			Robot robot = new Robot();

			// Capture the whole screen
			Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage bufferedImage = robot.createScreenCapture(area);

			// Write generated image to a file
			try {
				// Save as PNG
				File file = new File("screenshot.png");
				ImageIO.write(bufferedImage, "png", file);
			} catch (IOException e) {
				System.out.println("Could not save full screenshot " + e.getMessage());
			}
		} catch (AWTException e) {
			System.out.println("Could not capture screen " + e.getMessage());
		}
	}
}
