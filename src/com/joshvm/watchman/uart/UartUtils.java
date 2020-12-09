package com.joshvm.watchman.uart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class UartUtils {
	private String com = "COM0";
	private int baudrate = 9600;
	private byte[] cmd = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 };
	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public UartUtils(String com, int baudrate, byte[] cmd) {
		this.com = com;
		this.baudrate = baudrate;
		this.cmd = cmd;
	}

	public String read() {
		final StringBuffer receiveData = new StringBuffer();
		try {
			String host = "comm:" + com + ";baudrate=" + baudrate;
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
			outputStream.write(cmd, 0, 8);
//			System.out.println("send:" + byte2hex(cmd));
			// 启动子线程接收数据，避免read阻塞
			new Thread(new Runnable() {
				public void run() {
					try {
						byte[] buffer = new byte[256];
						int len = inputStream.read(buffer);
						receiveData.append(byte2hex(buffer, len));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}).start();
			Thread.sleep(3000);
//			System.out.println("readmsg: " + receiveData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return receiveData.toString();
	}

	private static void closeStream() throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		if (streamConnection != null) {
			streamConnection.close();
		}

	}

	private static String byte2hex(byte[] data) {
		return byte2hex(data, data.length);
	}

	private static String byte2hex(byte[] data, int len) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < len; ++i) {
			if (data[i] < 16 && data[i] > -1) {
				stringBuffer.append("0" + Integer.toHexString(data[i] & 255) + " ");
			} else {
				stringBuffer.append(Integer.toHexString(data[i] & 255) + " ");
			}
		}
		return stringBuffer.toString();
	}
}
