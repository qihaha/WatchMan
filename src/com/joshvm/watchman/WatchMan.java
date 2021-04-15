package com.joshvm.watchman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.core.GpioStatusThread;
import com.joshvm.watchman.core.GpsThread;
import com.joshvm.watchman.core.ModelCheckThread;
import com.joshvm.watchman.core.UartReadThread;
import com.joshvm.watchman.core.UartWriteThread;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.mqtt.Subscriber;
import com.joshvm.watchman.utils.FileUtils;
import com.joshvm.watchman.utils.GPIOUtils;

/**
 * WatchMan
 * <p>
 * 守望者-程序主入口。
 * 
 * @author Administrator
 *
 */
public class WatchMan {

	// 根据硬件及接线设定串口的com口
	private static String com = "COM0";
	// 波特率
	private static int baudrate = 9600;
	
	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args) {
		
		System.out.println("[flg] Start WatchMan ...");
		// 发布者建立连接,用来发送消息
		final Publisher publisher = new Publisher();
		publisher.connect();

		// 订阅者建立连接，用来接收指令和配置
		Subscriber subscriber = new Subscriber();
		subscriber.connect();

		if (!Constants.GPIO_STATUS){
			// gpio状态恢复
			System.out.println("[status] init");
			for (int i = 0; i < Constants.indexList.length; i++) {
				int currentIndex = Constants.indexList[i];
				String currentStatus = FileUtils.readGpioStatus(currentIndex);
				System.out.println("[status] gpio-" + currentIndex + ":" + currentStatus);
				GPIOUtils.gpioSwitch(currentIndex, currentStatus);
			}
			System.out.println("[status] init succeed");
		}

		// 开启串口数据检测
		try {
			String host = "comm:" + com + ";baudrate=" + baudrate;
			// 建立连接
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 串口水位、电表
		UartWriteThread wlThread = new UartWriteThread(outputStream);
		wlThread.start();
		// 获取串口结果结果推送
		UartReadThread uartReadThread = new UartReadThread(inputStream, publisher);
		uartReadThread.start();
		// gps数据
		GpsThread gpsThread = new GpsThread(publisher);
		gpsThread.start();
		// gpio上报状态
		GpioStatusThread gpioStatusThread = new GpioStatusThread(publisher);
		gpioStatusThread.start();
		// gpio model 检查&上报
		ModelCheckThread modelCheckThread = new ModelCheckThread(publisher);
		modelCheckThread.start();

		// 死循环，守护进程
		try {
			while (true) {
				Thread.sleep(1000 * 10);
				System.out.println("[flg] Alive WatchMan ...");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
