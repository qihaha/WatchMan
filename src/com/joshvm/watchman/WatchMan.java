package com.joshvm.watchman;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.mqtt.Subscriber;
import com.joshvm.watchman.uart.UartUtils;

/**
 * WatchMan
 * <p>
 * 守望者-程序主入口。
 * 
 * @author Administrator
 *
 */
public class WatchMan {

	private static final String COM = "COM1";
	private static final int BAUDRATE = 9600;

	public static final String SERVER_URI = "tcp://39.98.64.167:61613";
	public static final String CLIENT_ID = Utils.getImei();
	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "password";
	public static final String TOPIC = "testbroker";

	private static final String CODE = "0103010200012436";

	public static void main(String[] args) {

		System.out.println("Start WatchMan ...");
		final UartUtils uartUtils = new UartUtils(COM, BAUDRATE);
		// Publisher
		 
//		 Subscriber subscriber = new Subscriber(SERVER_URI,CLIENT_ID,USER_NAME,PASSWORD,TOPIC,null);
		// 启动子线程，每1s发送数据
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						uartUtils.writeData(CODE);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		while(true){
		try {
			Thread.sleep(1000);
			System.out.println("receive start");
			String message = uartUtils.readData();
			// push to mqtt.server
//			 publisher.push(TOPIC, "1222");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}


	}

}
