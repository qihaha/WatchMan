package com.joshvm.watchman.core;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.FileUtils;

public class EleThread extends Thread{

	private Publisher publisher;
	public EleThread(Publisher publisher){
		this.publisher=publisher;
	}
	
	public void run() {
		while (true) {
			try {
				String data = "1863.44";
				publisher.push(Constants.TOPIC_DATA, Constants.FLG_ELE + "," + data);
				System.out.println("[data] electricity:" + data);
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_ELE);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
