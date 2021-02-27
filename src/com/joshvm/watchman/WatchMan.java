package com.joshvm.watchman;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.mqtt.Subscriber;
import com.joshvm.watchman.utils.CommonUtils;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.GPIOUtils;
import com.joshvm.watchman.utils.GpsUtils;
import com.joshvm.watchman.utils.UartUtils;

/**
 * WatchMan
 * <p>
 * 守望者-程序主入口。
 * 
 * @author Administrator
 *
 */
public class WatchMan {

	private static final String COM = "COM0";
	private static final int BAUDRATE = 9600;
	private static final byte[] WL_BYTE = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; //"0103010200012436"
	// TODO ELE指令需要更新成正确的指令
	private static final byte[] ELE_BYTE = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; //"0103010200012436"

	public static final String SERVER_URI = "tcp://39.98.64.167:61613";
	public static final String CLIENT_ID = CommonUtils.getImei();
	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "password";
	public static final String TOPIC_DATA = "broker_data";
	public static final String TOPIC_ACTION = "broker_action";


	public static void main(String[] args) {

		System.out.println("[flg] Start WatchMan ...");
		
		// 状态恢复
		System.out.println("[status] init");
		// gpio状态恢复
		int[] indexList = {14,15,16,17};
		for(int i=0;i<indexList.length;i++){
			int currentIndex = indexList[i];
			String currentStatus = FileUtils.readGpioStatus(currentIndex);
			System.out.println("[status] gpio-"+currentIndex+":"+currentStatus);
			GPIOUtils.gpioSwitch(currentIndex, currentStatus);
		}
		// 状态恢复完成
		System.out.println("[status] succeed");
		
		// 启动接收端
		MqttCallback actionCallback = new MqttCallback() {
			public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
				String message = new String(arg1.getPayload());
				if(message.startsWith("01")){
					System.out.println("[config] current config:"+FileUtils.writeConfig(message));
				}else if(message.startsWith("02")){
					int index = CommonUtils.hexToDecimal(message.substring(2,4));
					String status = message.substring(4,6);
					GPIOUtils.gpioSwitch(index,status);
					System.out.println("[switch] gpiop:"+message);
				}else{
					System.out.println("[error] unknow action");
				}
			}

			public void deliveryComplete(IMqttDeliveryToken arg0) {
				
			}

			public void connectionLost(Throwable arg0) {
				System.out.println("[error] connectionLost: " + arg0.getMessage());
				arg0.printStackTrace();
			}
		};
		Subscriber subscriber = new Subscriber(SERVER_URI,CLIENT_ID,USER_NAME,PASSWORD,TOPIC_ACTION,actionCallback);
		subscriber.start();
		// 启动接收端完成
		
		
		//数据发送逻辑,每个数据都是一个独立的线程
		final Publisher publisher = new Publisher(WatchMan.SERVER_URI,WatchMan.CLIENT_ID,WatchMan.USER_NAME,WatchMan.PASSWORD);
		// gps数据
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String location = GpsUtils.getLocation();
						System.out.println("[data] gps:"+location);
						publisher.push(WatchMan.TOPIC_DATA, location);
						int sleepSecond = FileUtils.readConfig(FileUtils.GPIO_GPS);
						Thread.sleep(sleepSecond*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 水位数据
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String data = new UartUtils(WatchMan.COM,WatchMan.BAUDRATE,WatchMan.WL_BYTE).read();
						publisher.push(WatchMan.TOPIC_DATA, data);
						System.out.println("[data] waterLevel:"+data);
						int sleepSecond = FileUtils.readConfig(FileUtils.GPIO_WL);
						Thread.sleep(sleepSecond*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 电表数据
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String data = new UartUtils(WatchMan.COM,WatchMan.BAUDRATE,WatchMan.ELE_BYTE).read();
						publisher.push(WatchMan.TOPIC_DATA, data);
						System.out.println("[data] electricity:"+data);
						int sleepSecond = FileUtils.readConfig(FileUtils.GPIO_ELE);
						Thread.sleep(sleepSecond*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		
		//主线程死循环做守护进程
		while(true){
			try {
				System.out.println("[flg] Status WatchMan ...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
