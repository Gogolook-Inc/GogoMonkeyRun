
package com.gogolook.uicomparerunner.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

public class SocketClient {
	public static final int STAUTS_WAITING = 0x1000;
	public static final int STAUTS_CONNECTED = 0x1001;
	public static final int STAUTS_DISCONNECTED = 0x1002;
	private int status = STAUTS_WAITING;

	private String address = "192.168.23.21";// 連線的ip
	private int port = 8765;// 連線的port
	private Socket socket;
	private InetSocketAddress isa;
	private SocketStatusListener mSocketStatusListener;

	public interface SocketStatusListener {

		public void onStatusChanged();

		public void onSocketException(Exception e);

		public void onRead(byte[] readByte);
	}

	public interface OnWriteCallBackListener {
		public void onWriteCallBack();
	}

	public SocketClient(String address, int port, SocketStatusListener socketStatusListener) {
		this.address = address;
		this.port = port;
		mSocketStatusListener = socketStatusListener;
	}

	public SocketClient connect() {
		status = STAUTS_WAITING;
		socket = new Socket();
		isa = new InetSocketAddress(this.address, this.port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					socket.connect(isa, 10000);
					status = STAUTS_CONNECTED;
					if (mSocketStatusListener != null)
						mSocketStatusListener.onStatusChanged();
				} catch (IOException e) {
					e.printStackTrace();
					if (mSocketStatusListener != null)
						mSocketStatusListener.onSocketException(e);
				}
				read();
			}
		}).start();

		return this;
	}

	public SocketClient write(final String message) {
		return write(message, null);
	}

	public SocketClient write(final String message, final OnWriteCallBackListener onWriteCallBackListener) {
		return write(message.getBytes(), onWriteCallBackListener);
	}

	public SocketClient write(final byte[] bytes, final OnWriteCallBackListener onWriteCallBackListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					// 送出字串
					out.write(bytes);
					Log.w(SocketClient.class.getSimpleName(), "Write to Socket Success!");

					if (onWriteCallBackListener != null)
						onWriteCallBackListener.onWriteCallBack();
				} catch (java.io.IOException e) {
					Log.w(SocketClient.class.getSimpleName(), "Write to Socket Fail!");
					Log.w(SocketClient.class.getSimpleName(), "IOException :" + e.toString());
					if (mSocketStatusListener != null)
						mSocketStatusListener.onSocketException(e);
				}
			}
		}).start();

		return this;
	}

	public SocketClient read() {

		try {
			byte[] readByte = new byte[socket.getReceiveBufferSize()]; // 10,832
			int bytesRead = socket.getInputStream().read(readByte);

			if (bytesRead == -1) {
				close();
				return this;
			}

			if (mSocketStatusListener != null) {
				mSocketStatusListener.onRead(readByte);
			}

			read();
		} catch (java.io.IOException e) {
			Log.w(SocketClient.class.getSimpleName(), "Read from Socket Fail!");
			Log.w(SocketClient.class.getSimpleName(), "IOException :" + e.toString());
			if (mSocketStatusListener != null)
				mSocketStatusListener.onSocketException(e);
		}

		return this;
	}

	public void close() {
		write(SocketInstruction.SOCKET_CLOSE, new OnWriteCallBackListener() {

			@Override
			public void onWriteCallBack() {
				try {
					socket.close();
					status = STAUTS_DISCONNECTED;
					if (mSocketStatusListener != null)
						mSocketStatusListener.onStatusChanged();
				} catch (IOException e) {
					e.printStackTrace();
					if (mSocketStatusListener != null)
						mSocketStatusListener.onSocketException(e);
				}
				socket = null;
			}
		});

	}

	public boolean isWaiting() {
		return status == STAUTS_WAITING;
	}

	public boolean isConnected() {
		return status == STAUTS_CONNECTED;
	}
}
