package com.joshvm.watchman.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class UartDemoSet {
	// 根据硬件及接线设定串口的com口
	private static String com = "COM0";
	// 波特率
	private static int baudrate = 9600;

	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args) {
		System.out.println("       ============      Start Uart Demo...");

		String host = "comm:" + com + ";baudrate=" + baudrate;

		try {

			try {
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// 建立连接
			streamConnection = (StreamConnection) Connector.open(host);

			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();

			System.out.println("open ......");

			
			// 距离传感器，读距离 01 06 01 03 00 02 F9 F7
			byte[] data1 = new byte[] {(byte)0x01, (byte)0x06, (byte)0x01, (byte)0x03, (byte)0x00, (byte)0x02, (byte)0xF9, (byte)0xF7};
			
			new Thread(new Runnable() {

				public void run() {
					while (true) {
						try {
							int len = 0;
							byte[] buffer = new byte[256];
							StringBuffer stringBuffer = null;

							// 读数据
							while ((len = inputStream.read(buffer)) != -1) {
								System.out.println("read...");

								stringBuffer = new StringBuffer();
								for (int i = 0; i < len; i++) {
									if (buffer[i] < 16 && buffer[i] > -1) {
										stringBuffer.append("0" + Integer.toHexString(buffer[i] & 0xFF) + " ");
									} else {
										stringBuffer.append(Integer.toHexString(buffer[i] & 0xFF) + " ");
									}
								}
								System.out.println("++++receive:" + stringBuffer.toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			while (true) {
				
				sendData(data1, 0, data1.length);
				System.out.println("====send: data1");
				
				Thread.sleep(5 * 1000);

			}

		} catch (Exception e) {

			// 部分Exception说明
			if (e.getMessage().equals(com + " port not found!")) {

				System.out.println("E1:COM设置错误");

			} else if (e.getMessage().equals("Stream closed")) {

				System.out.println("E2:Stream关闭");
			}

			e.printStackTrace();

		} 

	}

	/**
	 * 发送数据
	 * 
	 * @param buffer
	 * @param off
	 * @param len
	 * @throws IOException
	 */
	private static void sendData(byte[] buffer, int off, int len) throws IOException {

		if (outputStream != null) {

			outputStream.write(buffer, off, len);

		}
	}

	/**
	 * 关闭串口连接
	 * <p>
	 * 如果是阻塞读数据的时候，可以由外部进行关闭
	 * 
	 * @throws IOException
	 */
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


}
