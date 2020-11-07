package com.joshvm.watchman;

import com.joshvm.watchman.mqtt.Subscriber;

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
	
	private static final String SERVER_URI = "tcp://127.0.0.1:61613";
	private static final String CLIENT_ID = Utils.getImei();
	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "password";
	private static final String TOPIC = "testbroker";
	
	private static final String CODE = "0103010200012436";

	public static void main(String[] args) {

		System.out.println("Start WatchMan ...");
//		final UartUtils uartUtils = new UartUtils(COM, BAUDRATE);
		// Publisher
//		Publisher publisher = new Publisher(SERVER_URI,CLIENT_ID,USER_NAME,PASSWORD);
		Subscriber subscriber = new Subscriber(SERVER_URI,CLIENT_ID,USER_NAME,PASSWORD,TOPIC,null);
		while (true) {
			try {
//				uartUtils.writeData(CODE);
				Thread.sleep(1000);
//				String message = uartUtils.readData();
//				System.out.println(message);
				// push to mqtt.server
//				publisher.push(TOPIC, "1122");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

}
