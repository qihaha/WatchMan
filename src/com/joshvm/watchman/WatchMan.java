package com.joshvm.watchman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.core.GpioStatusThread;
import com.joshvm.watchman.core.ModelCheckThread;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.CommonUtils;
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
		// final Publisher publisher = new Publisher();
		// publisher.connect();
		Publisher publisher = null;

		// 订阅者建立连接，用来接收指令和配置
		// Subscriber subscriber = new Subscriber();
		// subscriber.connect();

		// >>>状态恢复
		System.out.println("[status] init");
		// gpio状态恢复
		for (int i = 0; i < Constants.indexList.length; i++) {
			int currentIndex = Constants.indexList[i];
			String currentStatus = FileUtils.readGpioStatus(currentIndex);
			System.out.println("[status] gpio-" + currentIndex + ":" + currentStatus);
			GPIOUtils.gpioSwitch(currentIndex, currentStatus);
		}
		// gpio上报状态
		GpioStatusThread gpioStatusThread = new GpioStatusThread(publisher);
		gpioStatusThread.start();
		// gpio model 检查
		ModelCheckThread modelCheckThread = new ModelCheckThread();
		modelCheckThread.start();

		// <<<状态恢复完成
		System.out.println("[status] init succeed");

		// gps数据
		// GpsThread gpsThread = new GpsThread(publisher);
		// gpsThread.start();

		// 开启串口数据检测
		try {
			String host = "comm:" + com + ";baudrate=" + baudrate;
			// 建立连接
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
			final byte[] b = new byte[] { 0x01, 0x03, 0x01, 0x02, 0x00, 0x01, 0x24, 0x36 };
			// 水位
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							byte cmd_query[] = { 1, 3, 1, 2, 0, 1, 36, 54 };
							// outputStream.write(hexToByteArray("0103010200012436"));
							outputStream.write(cmd_query, 0, 8);
							outputStream.flush();

							byte cmd_query2[] = { 1, 3, 1, 2, 0, 1, 36, 54 };
							// outputStream.write(hexToByteArray("0103010200012436"));
							outputStream.write(cmd_query2, 0, 8);
							outputStream.flush();
							// System.out.println("send:{0x01,
							// 0x03,0x01,0x02,0x00,0x01,0x24,0x36}");
							int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_WL);
							Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}).start();

			// 电表
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {

							byte cmd_query[] = { 1, 3, 1, 2, 0, 1, 36, 54 };
							// outputStream.write(hexToByteArray("0103010200012436"));
							outputStream.write(cmd_query, 0, 8);
							outputStream.flush();
							// System.out.println("send:{0x01,
							// 0x03,0x01,0x02,0x00,0x01,0x24,0x36}");
							int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_ELE);
							Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}).start();

			// 自定义指令
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							if(Constants.CMD_CUSTOM.length()>16){
								byte cmd_query[] = new byte[8];
								for(int i=0;i<cmd_query.length;i++){
									cmd_query[i]=(byte) CommonUtils.hexToDecimal(Constants.CMD_CUSTOM.substring(i*2,(i+1)*2));
								}
								outputStream.write(cmd_query, 0, 8);
								outputStream.flush();
								System.out.println("[custom_cmd] run:"+Constants.CMD_CUSTOM);
								Constants.CMD_CUSTOM="";
							}
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}).start();

			int len = 0;
			byte[] buffer = new byte[7];
			// 读数据
			while ((len = inputStream.read(buffer)) != -1) {
				String data = CommonUtils.byte2hex(buffer, len);
				data = data.replace((char) 32, (char) 0);// 替换空格
				System.out.println("[data] waterLevel:" + data);
				if (data.length() >= 14) {
					String sendStr = "";
					String index = data.substring(0, 2);
					if ("1".equals(index)) {
						String value = data.substring(6, 10);
						sendStr = Constants.FLG_WL + "," + index + ":" + CommonUtils.hexToDecimal(value);
					} else if ("2".equals(index)) {
						String value = data.substring(6, 10);
						sendStr = Constants.FLG_WL + "," + index + ":" + CommonUtils.hexToDecimal(value);
					} else if ("3".equals(index)) {
						String value = data.substring(6, 10);
						sendStr = Constants.FLG_ELE + "," + index + ":" + CommonUtils.hexToDecimal(value);
					} else {
						System.out.println("[error] index_error:");
					}
					// publisher.push(Constants.TOPIC_DATA, sendStr);
					System.out.println("[data] readStr:" + sendStr);
				} else {
					System.out.println("[data] readStr: length error");
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
