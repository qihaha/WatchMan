package com.joshvm.watchman.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.joshvm.watchman.WatchMan;
import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.utils.CommonUtils;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.GPIOUtils;
import com.joshvm.watchman.utils.UartUtils;

/**
 * 订阅者
 * 
 */
public class Subscriber {

	private MqttClient mqttClient;

	public void connect() {
		try {
			mqttClient = new MqttClient(Constants.SERVER_URI, Constants.CLIENT_ID + 1, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(Constants.USER_NAME);
			options.setPassword(Constants.PASSWORD.toCharArray());
			mqttClient.setCallback(new MqttCallback() {
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					String message = new String(arg1.getPayload());
					if(message.startsWith("00")){
						Constants.CMD_CUSTOM = message.substring(2);
						System.out.println("[custom_cmd] cmd:"+message);
					}else if(message.startsWith("01")){
						System.out.println("[config] current config:"+FileUtils.writeConfig(message));
					}else if(message.startsWith("02")){
						int index = CommonUtils.hexToDecimal(message.substring(2,4));
						String status = message.substring(4,6);
						GPIOUtils.gpioSwitch(index,status);
						System.out.println("[switch] gpiop:"+message);
						//手动模式开启
						FileUtils.writeModel(Constants.MODEL_DEFULT);
					}else if(message.startsWith("03")){
						// 其他模式开启
						String modelStr = message.substring(2,6);
						FileUtils.writeModel(modelStr);
						if(modelStr.endsWith(Constants.MODEL_STOP)){
							//停止模式,便利所有gpio停止
							for (int i = 0; i < Constants.indexList.length; i++) {
								int currentIndex = Constants.indexList[i];
								GPIOUtils.gpioSwitch(currentIndex, Constants.GPIO_OFF);
							}
						}else if(modelStr.endsWith(Constants.MODEL_XIAOHUA)){
							//消化模式
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_XUNHUAN, Constants.GPIO_OFF);
							GPIOUtils.gpioSwitch(Constants.GPIO_CHU, Constants.GPIO_ON);
						}else if(modelStr.endsWith(Constants.MODEL_TUODAN)){
							// 脱氮模式
							GPIOUtils.gpioSwitch(Constants.GPIO_JIN1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_BAO1, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_XUNHUAN, Constants.GPIO_ON);
							GPIOUtils.gpioSwitch(Constants.GPIO_CHU, Constants.GPIO_ON);
						}else{
							System.out.println("[error] unknow modelStr");
						}
					}else{
						System.out.println("[error] unknow action");
					}
					
				}

				public void deliveryComplete(IMqttDeliveryToken arg0) {
					
				}

				public void connectionLost(Throwable arg0) {
					
				}
			});

			mqttClient.connect(options);

			String topic = Constants.TOPIC_ACTION;

			mqttClient.subscribe(topic, 1);

		} catch (MqttException e) {

			e.printStackTrace();
		}
	}
}
