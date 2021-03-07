package com.joshvm.watchman;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.core.EleThread;
import com.joshvm.watchman.core.GpsThread;
import com.joshvm.watchman.core.WlThread;
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

	
	public static void main(String[] args) {

		System.out.println("[flg] Start WatchMan ...");

		// >>>状态恢复
		System.out.println("[status] init");
		// gpio状态恢复
		int[] indexList = { 10, 14, 15, 16, 17 };
		for (int i = 0; i < indexList.length; i++) {
			int currentIndex = indexList[i];
			String currentStatus = FileUtils.readGpioStatus(currentIndex);
			System.out.println("[status] gpio-" + currentIndex + ":" + currentStatus);
			GPIOUtils.gpioSwitch(currentIndex, currentStatus);
		}
		// <<<状态恢复完成
		System.out.println("[status] init succeed");

		// 发布者建立连接,用来发送消息
		final Publisher publisher = new Publisher();
		publisher.connect();

		// 订阅者建立连接，用来接收指令和配置
		Subscriber subscriber = new Subscriber();
		subscriber.connect();

		// 数据发送逻辑,每个数据都是一个独立的线程
		// 水位01数据
		WlThread wlThread1 = new WlThread(publisher,1);
		wlThread1.start();

		// 水位02数据
		WlThread wlThread2 = new WlThread(publisher,2);
		wlThread2.start();
		
		// 电表数据
		EleThread eleThread = new EleThread(publisher);
		eleThread.start();
		
		// gps数据
		GpsThread gpsThread = new GpsThread(publisher);
		gpsThread.start();

		// 主线程死循环做守护进程,响应订阅者指令
		while (true) {
			try {
				// TODO 线程异常检查
				Thread.sleep(60 * 1000);
				System.out.println("[flg] Check WatchMan ...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
