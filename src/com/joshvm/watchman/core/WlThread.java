package com.joshvm.watchman.core;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.CommonUtils;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.UartUtils;

public class WlThread extends Thread{

	private Publisher publisher;
	private int index;
	private byte[] cmd;
	public WlThread(Publisher publisher,int index){
		this.publisher=publisher;
		this.index = index;
		switch(index){
		case 1:
			cmd = Constants.WL_BYTE_01;
			break;
		case 2:
			cmd = Constants.WL_BYTE_02;
			break;
		default:
			break;
		}
	}
	
	public void run() {
		while (true) {
			try {
				String data = new UartUtils(Constants.COM, Constants.BAUDRATE, cmd).sendCmdRead();
				data = data.replace((char) 32, (char) 0);// 替换空格
				System.out.println("[data] waterLevel:" + data);
				if (data.length() >= 14) {
					// String index = data.substring(0, 2);
					String value = data.substring(6, 10);
					String sendStr = Constants.FLG_WL + "," + index + ":" + CommonUtils.hexToDecimal(value);
					publisher.push(Constants.TOPIC_DATA, sendStr);
					System.out.println("[data] waterLevel-sendStr:" + sendStr);
				} else {
					System.out.println("[data] waterLevel-sendStr: length error");
				}
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_WL);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}
