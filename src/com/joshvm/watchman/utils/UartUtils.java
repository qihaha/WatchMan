package com.joshvm.watchman.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class UartUtils {
	private static StreamConnection streamConnection;
	private InputStream inputStream;
	private OutputStream outputStream;

	public UartUtils(String com, int baudrate) {
		try {
			String host = "comm:" + com + ";baudrate=" + baudrate;
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendCmd(byte[] cmd) {
		try {
			outputStream.write(cmd, 0, cmd.length);
			System.out.println("[debug] sendCmd:" + cmd);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public InputStream getInputStream(){
		return inputStream;
	}
	
/*	public String readCmd() {
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
	}*/

	private void closeStream() throws IOException {
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

}
