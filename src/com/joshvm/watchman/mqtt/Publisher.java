package com.joshvm.watchman.mqtt;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher {

	private MqttClient mqttClient;

	public Publisher(String serverUri, String clientId, String userName, String password) {
		try {
			// 建立连接，ClientId为唯一标识
			mqttClient = new MqttClient(serverUri, clientId, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			mqttClient.connect(options);
			System.out.println("Publisher MQTT Connect Server Succeed");
		} catch (MqttException e) {

			e.printStackTrace();
		}
	}

	/**
	 * push data
	 * 
	 * @param topic
	 * @param message
	 */
	public void push(String topic, String message) {

		try {
			System.out.println(message);
			MqttMessage mqttMessage = new MqttMessage(message.getBytes("utf-8"));
			mqttMessage.setQos(1);
			mqttMessage.setRetained(false);
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
