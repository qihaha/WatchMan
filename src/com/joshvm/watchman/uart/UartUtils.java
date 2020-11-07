package com.joshvm.watchman.uart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * UartUtils
 * <p>
 * 实现串口通信。
 * 
 * @author Administrator
 *
 */
public class UartUtils {

	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public UartUtils(String com, int baudrate) {
		// 建立连接
		String host = "comm:" + com + ";baudrate=" + baudrate;
		try {
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readData() {
		String message = "";
		try {
			int len = 0;
			byte[] buffer = new byte[256];
			while ((len = inputStream.read(buffer)) != -1) {
				message += new String(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public boolean writeData(String message) {
		try {
			outputStream.write(message.getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
