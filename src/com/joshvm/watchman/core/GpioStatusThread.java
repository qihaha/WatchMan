package com.joshvm.watchman.core;

import java.util.Date;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.FileUtils;

public class GpioStatusThread extends Thread{

	private Publisher publisher;
	public GpioStatusThread(Publisher publisher){
		this.publisher=publisher;
	}
	
	public void run() {
		while (true) {
			Date currentDate = new Date();
			try {
				String statusStr = "";
				for (int i = 0; i < Constants.indexList.length; i++) {
					int currentIndex = Constants.indexList[i];
					String currentStatus = FileUtils.readGpioStatus(currentIndex);
					statusStr+=currentIndex+":"+currentStatus+"-"+currentDate.getTime()+","; 
				}
				
				System.out.println("[data] gpio:" + statusStr);
//				publisher.push(Constants.TOPIC_DATA, Constants.FLG_GPIO + "," + statusStr);
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_GPIO);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
