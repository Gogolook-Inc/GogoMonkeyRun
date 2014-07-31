
package com.gogolook.uicomparerunner.views.layout;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.james.views.FreeEditText;
import com.james.views.FreeLayout;
import com.james.views.FreeTextButton;
import com.james.views.FreeTextView;

public class GmMainLayout extends FreeLayout {

	private FreeLayout connectLayout;
	public FreeEditText addressEdit;
	public FreeEditText portEdit;
	public FreeTextButton connectButton;
	public FreeTextView deviceText;
	public FreeTextView socketText;
	public ImageView simulationImage;
	private FreeLayout bottomLayout;
	public FreeTextButton selectDeviceButton;
	public FreeTextButton selectScriptButton;
	public FreeTextButton selectApkButton;

	public GmMainLayout(Context context) {
		super(context);

		connectLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				new int[] {
					ALIGN_PARENT_TOP, CENTER_HORIZONTAL
				});

		addressEdit = (FreeEditText) connectLayout.addFreeView(new FreeEditText(context),
				350, LayoutParams.WRAP_CONTENT,
				new int[] {
					ALIGN_PARENT_TOP
				});
		addressEdit.setText("192.168.43.143");
		addressEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		setPadding(addressEdit, 15, 0, 15, 0);

		portEdit = (FreeEditText) connectLayout.addFreeView(new FreeEditText(context),
				250, LayoutParams.WRAP_CONTENT,
				addressEdit,
				new int[] {
					BELOW
				});
		portEdit.setText("8765");
		portEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		setPadding(portEdit, 15, 0, 15, 0);

		connectButton = (FreeTextButton) connectLayout.addFreeView(new FreeTextButton(context),
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				new int[] {
					ALIGN_PARENT_TOP
				},
				addressEdit,
				new int[] {
					RIGHT_OF
				});
		connectButton.setText("Connect");
		connectButton.setTextSizeFitSp(25);
		setPadding(connectButton, 15, 5, 15, 5);

		deviceText = (FreeTextView) this.addFreeView(new FreeTextView(context),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
				connectLayout,
				new int[] {
					BELOW
				});
		deviceText.setTextSizeFitSp(20);
		deviceText.setGravity(Gravity.CENTER);
		deviceText.setText("Current Device: null");
		deviceText.setTextColor(Color.WHITE);
		deviceText.setBackgroundColor(0xff3b3033);
		setMargin(deviceText, 25, 10, 25, 0);

		bottomLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				new int[] {
					ALIGN_PARENT_BOTTOM, CENTER_HORIZONTAL
				});

		selectDeviceButton = (FreeTextButton) bottomLayout.addFreeView(new FreeTextButton(context),
				320, LayoutParams.WRAP_CONTENT);
		selectDeviceButton.setText("Select a Device");
		selectDeviceButton.setTextSizeFitSp(25);
		selectDeviceButton.setEnabled(false);
		setPadding(selectDeviceButton, 15, 5, 15, 5);

		selectScriptButton = (FreeTextButton) bottomLayout.addFreeView(new FreeTextButton(context),
				320, LayoutParams.WRAP_CONTENT,
				selectDeviceButton,
				new int[] {
					RIGHT_OF
				});
		selectScriptButton.setText("Select a Script");
		selectScriptButton.setTextSizeFitSp(25);
		selectScriptButton.setEnabled(false);
		setPadding(selectScriptButton, 15, 5, 15, 5);

		selectApkButton = (FreeTextButton) bottomLayout.addFreeView(new FreeTextButton(context),
				640, LayoutParams.WRAP_CONTENT,
				selectDeviceButton,
				new int[] {
					BELOW
				});
		selectApkButton.setText("Select an APK");
		selectApkButton.setTextSizeFitSp(25);
		selectApkButton.setEnabled(false);
		setPadding(selectApkButton, 15, 5, 15, 5);

		FreeLayout simulationLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
				600, 400,
				new int[] {
					CENTER_IN_PARENT
				});

		simulationImage = (ImageView) simulationLayout.addFreeView(new ImageView(context),
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				new int[] {
					CENTER_IN_PARENT
				});
		simulationImage.setScaleType(ScaleType.FIT_CENTER);

		socketText = (FreeTextView) simulationLayout.addFreeView(new FreeTextView(context),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
				new int[] {
					CENTER_IN_PARENT
				});
		socketText.setTextSizeFitSp(20);
		socketText.setGravity(Gravity.CENTER);
		socketText.setText("Waiting");
		socketText.setTextColor(Color.WHITE);
		socketText.setBackgroundColor(0xaa3b3033);
		setMargin(socketText, 25, 10, 25, 0);
	}
}
