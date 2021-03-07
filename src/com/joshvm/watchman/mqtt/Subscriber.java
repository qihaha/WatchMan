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
					if(message.startsWith("01")){
						System.out.println("[config] current config:"+FileUtils.writeConfig(message));
					}else if(message.startsWith("02")){
						int index = CommonUtils.hexToDecimal(message.substring(2,4));
						String status = message.substring(4,6);
						GPIOUtils.gpioSwitch(index,status);
						System.out.println("[switch] gpiop:"+message);
					}else if(message.startsWith("03")){
						// TODO 设置模组地址？
//						byte[] cmd = {};// TODO
//						new UartUtils(WatchMan.COM,WatchMan.BAUDRATE,cmd).sendCmd();
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
