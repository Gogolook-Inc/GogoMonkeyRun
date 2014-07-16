
package com.gogolook.uicomparerunner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.gogolook.uicomparerunner.socket.SocketClient;
import com.gogolook.uicomparerunner.socket.SocketClient.OnWriteCallBackListener;
import com.gogolook.uicomparerunner.socket.SocketClient.SocketStatusListener;
import com.gogolook.uicomparerunner.socket.SocketInstruction;
import com.gogolook.uicomparerunner.views.layout.GmMainLayout;
import com.james.utils.LogUtils;
import com.james.views.FreeEditText;
import com.james.views.FreeTextButton;
import com.james.views.FreeTextView;
import com.james.views.dialog.DialogBuilder;

public class GmMainActivity extends Activity implements OnClickListener {
	private GmMainLayout mainLayout;
	private FreeEditText addressEdit;
	private FreeEditText portEdit;
	private FreeTextButton connectButton;
	private FreeTextView deviceText;
	private FreeTextView socketText;
	private ImageView simulationImage;
	private FreeTextButton selectDeviceButton;
	private FreeTextButton selectScriptButton;
	private FreeTextButton selectApkButton;

	private SocketClient socketClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

		findView();

		setView();

		setListener();

	}

	private void setLayout() {
		mainLayout = new GmMainLayout(this);
		setContentView(mainLayout);
	}

	private void findView() {
		addressEdit = mainLayout.addressEdit;
		portEdit = mainLayout.portEdit;
		connectButton = mainLayout.connectButton;
		deviceText = mainLayout.deviceText;
		socketText = mainLayout.socketText;
		simulationImage = mainLayout.simulationImage;
		selectDeviceButton = mainLayout.selectDeviceButton;
		selectScriptButton = mainLayout.selectScriptButton;
		selectApkButton = mainLayout.selectApkButton;
	}

	private void setView() {

	}

	private void setListener() {
		connectButton.setOnClickListener(this);
		selectDeviceButton.setOnClickListener(this);
		selectScriptButton.setOnClickListener(this);
		selectApkButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(connectButton)) {
			if (connectButton.getText().toString().equalsIgnoreCase("Connect")) {
				initSocket();
				socketClient.connect();
			}
			else {
				socketClient.close();
			}
		}
		else if (v.equals(selectDeviceButton)) {
			socketClient.write(SocketInstruction.GET_ALL_DEVICES);
		}
		else if (v.equals(selectScriptButton)) {
			socketClient.write(SocketInstruction.GET_ALL_SCRIPTS);
		}
		else if (v.equals(selectApkButton)) {
			socketClient.write(SocketInstruction.GET_ALL_APKS);
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (socketClient != null) {
			socketClient.close();
		}
	}

	private void initSocket() {
		String address = addressEdit.getText().toString();
		int port = Integer.parseInt(portEdit.getText().toString());
		socketClient = new SocketClient(address, port, new SocketStatusListener() {

			@Override
			public void onStatusChanged() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (socketClient == null)
							return;
						if (socketClient.isWaiting()) {
							socketText.setText("Waiting");
							connectButton.setText("Connect");
							selectDeviceButton.setEnabled(false);
							selectScriptButton.setEnabled(false);
							selectApkButton.setEnabled(false);
						}
						else if (socketClient.isConnected()) {
							socketText.setText("Connected");
							connectButton.setText("Disconnect");
							selectDeviceButton.setEnabled(true);
							selectScriptButton.setEnabled(true);
							selectApkButton.setEnabled(true);
						}
						else {
							socketText.setText("Disconnected...");
							deviceText.setText("Current Device: null");
							connectButton.setText("Connect");
							selectDeviceButton.setEnabled(false);
							selectScriptButton.setEnabled(false);
							selectApkButton.setEnabled(false);
						}
					}
				});
			}

			@Override
			public void onSocketException(final Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(GmMainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onRead(final byte[] readByte) {
				runOnUiThread(new Runnable() {
					public void run() {
						String recieve = new String(readByte).trim();

						if (recieve.startsWith(SocketInstruction.SOCKET_CONNECTED)) {
							// TODO
							deviceText.setText("Current Device: " + recieve.split(SocketInstruction.separator)[1]);
						}
						else if (recieve.startsWith(SocketInstruction.GET_ALL_DEVICES)) {
							selectDevice(recieve.split(SocketInstruction.separator)[1]);
						}
						else if (recieve.startsWith(SocketInstruction.GET_ALL_SCRIPTS)) {
							selectScript(recieve.split(SocketInstruction.separator)[1]);
						}
						else if (recieve.startsWith(SocketInstruction.COMPLETE_RUNNING_SCRIPT)) {
							socketText.setText("Auto-testing completed.");
						}
						else if (recieve.startsWith(SocketInstruction.GET_ALL_APKS)) {
							selectApk(recieve.split(SocketInstruction.separator)[1]);
						}
						else if (recieve.startsWith(SocketInstruction.COMPLETE_INSTALLING_APK)) {
							socketText.setText("Install new APK completed.");
						}
						else if (recieve.startsWith(SocketInstruction.ERROR_MESSAGE)) {
							Toast.makeText(GmMainActivity.this, recieve.split(SocketInstruction.separator)[1], Toast.LENGTH_SHORT).show();
						}
						else {
							setScreenshotToImageView(readByte);
						}
					}
				});
			}
		});
	}

	private void selectDevice(String recieve) {
		final ArrayList<Pair<String, String>> items = getAllDevices(recieve);

		DialogBuilder.showSingleSelectDialog(GmMainActivity.this, "Select a Device", items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, final int which) {
				socketClient.write(SocketInstruction.SET_DEVICE + SocketInstruction.separator + items.get(which).second, new OnWriteCallBackListener() {

					@Override
					public void onWriteCallBack() {
						runOnUiThread(new Runnable() {
							public void run() {
								socketText.setText("Select: " + items.get(which).second);
							}
						});
					}
				});
			}
		});
	}

	private ArrayList<Pair<String, String>> getAllDevices(String text) {

		String[] splits = text.split("\n");
		ArrayList<Pair<String, String>> devices = new ArrayList<Pair<String, String>>();
		for (String split : splits) {
			if (split.contains("	")) {
				String device = split.split("	")[0];
				devices.add(new Pair<String, String>(device, device));
			}
		}

		return devices;
	}

	private void selectScript(String recieve) {
		final ArrayList<Pair<String, String>> items = getAllItems(recieve);

		DialogBuilder.showSingleSelectDialog(GmMainActivity.this, "Select a Script", items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, final int which) {
				socketClient.write(SocketInstruction.SET_SCRIPT + SocketInstruction.separator + items.get(which).second, new OnWriteCallBackListener() {

					@Override
					public void onWriteCallBack() {
						runOnUiThread(new Runnable() {
							public void run() {
								socketText.setText("Start running: " + items.get(which).second);
							}
						});
					}
				});
			}
		});
	}

	private void selectApk(String recieve) {
		final ArrayList<Pair<String, String>> items = getAllItems(recieve);

		DialogBuilder.showSingleSelectDialog(GmMainActivity.this, "Select a APK to Install", items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, final int which) {
				socketClient.write(SocketInstruction.SET_APK + SocketInstruction.separator + items.get(which).second, new OnWriteCallBackListener() {

					@Override
					public void onWriteCallBack() {
						runOnUiThread(new Runnable() {
							public void run() {
								socketText.setText("Start installing: " + items.get(which).second);
							}
						});
					}
				});
			}
		});
	}

	private ArrayList<Pair<String, String>> getAllItems(String text) {

		String[] splits = text.split(",");
		ArrayList<Pair<String, String>> scripts = new ArrayList<Pair<String, String>>();
		for (String script : splits) {
			scripts.add(new Pair<String, String>(script, script));
		}

		return scripts;
	}

	private void setScreenshotToImageView(byte[] readByte) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = true;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

		LogUtils.i(getClass().getSimpleName(), "bytes.length: " + readByte.length);
		Bitmap bitmap = BitmapFactory.decodeByteArray(readByte, 0, readByte.length, opt);
		simulationImage.setImageBitmap(bitmap);
	}
}
