package com.joshvm.watchman.core;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.GPIOUtils;

public class ModelCheckThread extends Thread {
	
	private Publisher publisher;
	public ModelCheckThread(Publisher publisher){
		this.publisher=publisher;
	}

	public void run() {
		int second = 1;
		while (true) {
			try {
				String modelStr = FileUtils.readModel();
				if(modelStr.length()==0){
					modelStr = Constants.MODEL_DEFULT;
				}
				if (!Constants.MODEL_DEFULT.equals(modelStr) && modelStr.startsWith(Constants.MODEL_DOUBLE)) {
					// 非手动模式,并且双泵需要执行切换
					if (second % Constants.MODEL_SWITCH_TIME == 0) {
						// 达到切换时间执行
						second = 0;
						System.out.println("[model] switch:" + modelStr);
						String jin01 = FileUtils.readGpioStatus(Constants.GPIO_JIN1);
						String jin02 = FileUtils.readGpioStatus(Constants.GPIO_JIN2);
						String bao01 = FileUtils.readGpioStatus(Constants.GPIO_BAO1);
						String bao02 = FileUtils.readGpioStatus(Constants.GPIO_BAO2);
						if (Constants.GPIO_STATUS){
							// gpio获取状态
							jin01 = GPIOUtils.gpioStatus(Constants.GPIO_JIN1);
							jin02 = GPIOUtils.gpioStatus(Constants.GPIO_JIN2);
							bao01 = GPIOUtils.gpioStatus(Constants.GPIO_BAO1);
							bao02 = GPIOUtils.gpioStatus(Constants.GPIO_BAO2);
						}
						if (Constants.GPIO_ON.endsWith(jin01)) {
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN1, Constants.GPIO_OFF);
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN2, Constants.GPIO_ON);
						} else if (Constants.GPIO_ON.endsWith(jin02)) {
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN2, Constants.GPIO_OFF);
						}
						if (Constants.GPIO_ON.endsWith(bao01)) {
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO1, Constants.GPIO_OFF);
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO2, Constants.GPIO_ON);
						} else if (Constants.GPIO_ON.endsWith(bao02)) {
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO2, Constants.GPIO_OFF);
						}
					}
					second++;
				}
				String sendStr = Constants.CLIENT_ID+","+Constants.FLG_MODEL + "," + modelStr;
				System.out.println("[data] model:" + sendStr);
				if(Constants.SEND_MQTT){
					publisher.push(Constants.TOPIC_DATA, sendStr);
				}
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_MODEL);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
