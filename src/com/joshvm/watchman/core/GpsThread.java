package com.joshvm.watchman.core;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.FileUtils;

public class GpsThread extends Thread{

	private Publisher publisher;
	public GpsThread(Publisher publisher){
		this.publisher=publisher;
	}
	
	public void run() {
		while (true) {
			try {
				// String location = GpsUtils.getLocation();
				String location = "129.999999,102.999999";
				System.out.println("[data] gps:" + location);
				publisher.push(Constants.TOPIC_DATA, Constants.FLG_GPS + "," + location);
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_GPS);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
