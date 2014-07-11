
package com.james.uicomparerunner.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	public static final int STAUTS_WAITING = 0x1000;
	public static final int STAUTS_CONNECTED = 0x1001;
	public static final int STAUTS_DISCONNECTED = 0x1002;
	private int status = STAUTS_WAITING;

	private ServerSocket server;
	private Socket socket;
	private final int serverPort = 8765;// 要監控的port
	private SocketStatusListener mSocketStatusListener;

	public interface SocketStatusListener {

		public void onStatusChanged();

		public void onSocketException(Exception e);

		public void onRead(String recieve);
	}

	public interface OnWriteCallBackListener {
		public void onWriteCallBack();
	}

	public SocketServer(SocketStatusListener socketStatusListener) {
		mSocketStatusListener = socketStatusListener;
	}

	public SocketServer waitForConnection() {
		try {
			server = new ServerSocket(serverPort);
			status = STAUTS_WAITING;
			if (mSocketStatusListener != null)
				mSocketStatusListener.onStatusChanged();
			System.out.println("Socket Initialized Success");
		} catch (java.io.IOException e) {
			System.out.println("Socket Initialized Fail!");
			System.out.println("IOException :" + e.toString());
			if (mSocketStatusListener != null)
				mSocketStatusListener.onSocketException(e);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (socket == null || !socket.isConnected()) {
					try {
						socket = server.accept();
						System.out.println("Connected from : InetAddress = " + socket.getInetAddress());
						status = STAUTS_CONNECTED;
						if (mSocketStatusListener != null)
							mSocketStatusListener.onStatusChanged();
					} catch (IOException e) {
						e.printStackTrace();
						if (mSocketStatusListener != null)
							mSocketStatusListener.onSocketException(e);
					}
				}
				read();
			}
		}).start();

		return this;
	}

	public SocketServer write(final String message) {
		return write(message, null);
	}

	public SocketServer write(final String message, final OnWriteCallBackListener onWriteCallBackListener) {
		return write(message.getBytes(), onWriteCallBackListener);
	}

	public SocketServer write(final byte[] bytes, final OnWriteCallBackListener onWriteCallBackListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					// 送出字串
					out.write(bytes);
					System.out.println("Write to Socket Success!");

					if (onWriteCallBackListener != null)
						onWriteCallBackListener.onWriteCallBack();
				} catch (java.io.IOException e) {
					System.out.println("Write to Socket Fail!");
					System.out.println("IOException :" + e.toString());
					if (mSocketStatusListener != null)
						mSocketStatusListener.onSocketException(e);
				}
			}
		}).start();

		return this;
	}

	public SocketServer read() {

		try {
			byte[] readByte = new byte[1024];
			int bytesRead = socket.getInputStream().read(readByte);

			if (bytesRead == -1) {
				close();
				return this;
			}

			String recieve = new String(readByte).trim();

			if (mSocketStatusListener != null)
				mSocketStatusListener.onRead(recieve);

			if (!recieve.equalsIgnoreCase(SocketInstruction.SOCKET_CLOSE)) {
				read();
			}
		} catch (java.io.IOException e) {
			System.out.println("Read from Socket Fail!");
			System.out.println("IOException :" + e.toString());
			if (mSocketStatusListener != null)
				mSocketStatusListener.onSocketException(e);
		}

		return this;
	}

	public void close() {
		//
		status = STAUTS_DISCONNECTED;
		if (mSocketStatusListener != null)
			mSocketStatusListener.onStatusChanged();
		try {
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (mSocketStatusListener != null)
				mSocketStatusListener.onSocketException(e);
		}
		socket = null;
		server = null;
	}

	public boolean isWaiting() {
		return status == STAUTS_WAITING;
	}

	public boolean isConnected() {
		return status == STAUTS_CONNECTED;
	}

	public String getInetAddress() {
		return socket.getInetAddress().toString();
	}
}
