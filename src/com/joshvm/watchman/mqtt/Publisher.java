package com.joshvm.watchman.mqtt;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.joshvm.watchman.constant.Constants;

/**
 * 发布者
 * 
 */
public class Publisher {

	private MqttClient mqttClient;

	/**
	 * 建立连接
	 */
	public void connect() {
		try {
			// 建立连接，ClientId为唯一标识
			mqttClient = new MqttClient(Constants.SERVER_URI, Constants.CLIENT_ID, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(Constants.USER_NAME);
			options.setPassword(Constants.PASSWORD.toCharArray());
			mqttClient.connect(options);
			System.out.println("[flg] publsher connected");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推送数据
	 * 
	 * @param topic
	 * @param message
	 */
	public void push(String topic, String message) {
		try {
			MqttMessage mqttMessage = new MqttMessage(message.getBytes("utf-8"));
			mqttMessage.setQos(1);
			mqttClient.publish(topic, mqttMessage);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
