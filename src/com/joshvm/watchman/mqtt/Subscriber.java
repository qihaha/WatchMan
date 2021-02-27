package com.joshvm.watchman.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Subscriber {

	private MqttClient mqttClient;
	private MqttConnectOptions options;
	private String topic;
	

	public Subscriber(String serverUri, String clientId, String userName, String password, String topic, MqttCallback callback) {
		try {
			this.topic = topic;
			
			options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			
			mqttClient = new MqttClient(serverUri, clientId, new MemoryPersistence());
			mqttClient.setCallback(callback==null?new MqttCallback() {
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					System.out.println("Client Topic: " + arg0);
					System.out.println("Client Qos : " + arg1.getQos());
					System.out.println("Client Message : " + new String(arg1.getPayload()));
				}

				public void deliveryComplete(IMqttDeliveryToken arg0) {
					System.out.println("Client Message ok: " + arg0);
				}

				public void connectionLost(Throwable arg0) {
					System.out.println("connectionLost: " + arg0.getMessage());
					arg0.printStackTrace();
				}
			}:callback);

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			mqttClient.connect(options);
			mqttClient.subscribe(topic, 1);
			System.out.println("[flg] Start Subscriber ...");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
