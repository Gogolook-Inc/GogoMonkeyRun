
package com.james.uicomparerunner.ui.view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CompareView extends JPanel {
	public ImageView targetView;
	public ImageView testView;
	public ImageView resultView;

	public CompareView(String title, ImageView targetView, ImageView testView, ImageView resultView) {
		super();
		this.setLayout(new FlowLayout());
		this.setBounds(0, 0, targetView.getWidth(), targetView.getHeight() * 2);

		JPanel textboxPanel = new JPanel(new FlowLayout());
		textboxPanel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(title),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		textboxPanel.add(targetView);
		targetView.setBounds(0, 0, targetView.getWidth(), targetView.getHeight());
		targetView.setVisible(true);

		if (testView != null) {
			textboxPanel.add(testView);
			testView.setBounds(targetView.getWidth() + targetView.getX(), targetView.getY() + targetView.getHeight(), targetView.getWidth(), targetView.getHeight());
			testView.setVisible(true);
		}

		if (resultView != null) {
			textboxPanel.add(resultView);
			resultView.setBounds(testView.getWidth() + testView.getX(), testView.getY() + testView.getHeight(), targetView.getWidth(), targetView.getHeight());
			resultView.setVisible(true);
		}

		this.add(textboxPanel);
		textboxPanel.setVisible(true);

		this.targetView = targetView;
		this.testView = testView;
		this.resultView = resultView;
	}

	public void clear() {
		removeAll();
		SwingUtilities.updateComponentTreeUI(this);
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();
	}
}
