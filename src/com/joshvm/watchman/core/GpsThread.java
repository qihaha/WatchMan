package com.joshvm.watchman.core;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.GpsUtils;

public class GpsThread extends Thread{

	private Publisher publisher;
	public GpsThread(Publisher publisher){
		this.publisher=publisher;
	}
	
	public void run() {
		while (true) {
			try {
				String location = GpsUtils.getLocation();
				if(location.startsWith("latitude")){
					// latitude:40.911624,longitude:117.96931877
					 String latitude = location.substring(location.indexOf("latitude:")+9,location.indexOf(","));
					 String longitude = location.substring(location.indexOf("longitude:")+10);
					 location = latitude+","+longitude;
				}else{
					location = "0.999999,0.999999";
				}
				String sendStr = Constants.CLIENT_ID+","+Constants.FLG_GPS + "," + location;
				System.out.println("[data] gps:" + sendStr);
				if(Constants.SEND_MQTT){
					publisher.push(Constants.TOPIC_DATA, sendStr);
				}
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_GPS);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
