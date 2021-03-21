package com.joshvm.watchman.core;

import java.io.InputStream;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.mqtt.Publisher;
import com.joshvm.watchman.utils.CommonUtils;

public class UartReadThread extends Thread{

	private Publisher publisher;
	private InputStream inputStream;
	public UartReadThread(InputStream inputStream, Publisher publisher){
		this.inputStream=inputStream;
		this.publisher=publisher;
	}
	
	public void run() {
		while (true) {
			try {
				int len = 0;
				byte[] buffer = new byte[256];
				StringBuffer stringBuffer = null;

				// 读数据
				while ((len = inputStream.read(buffer)) != -1) {
					stringBuffer = new StringBuffer();
					for (int i = 0; i < len; i++) {
						if (buffer[i] < 16 && buffer[i] > -1) {
							stringBuffer.append("0" + Integer.toHexString(buffer[i] & 0xFF) + " ");
						} else {
							stringBuffer.append(Integer.toHexString(buffer[i] & 0xFF) + " ");
						}
					}
					String data = stringBuffer.toString();
					if(Constants.DEBUG){
						System.out.println("[debug] uartStr:" + data);
					}
//					data = data.replace((char) 32, (char) 0);// 替换空格,char的0还是占位置
					if (data.length() >= 14) {
						String dataStr = "";
						String index = data.substring(0, 2);
						String value = data.substring(9,11)+data.substring(12,14);
						if(Constants.DEBUG){
							System.out.println("[debug] index:" + index);
							System.out.println("[debug] value:" + value);
						}
						if ("01".equals(index)) {
							dataStr = Constants.FLG_ELE + "," + index + ":" + CommonUtils.hexToDecimal(value);
						} else if ("02".equals(index)) {
							dataStr = Constants.FLG_WL + "," + index + ":" + CommonUtils.hexToDecimal(value);
						} else if ("03".equals(index)) {
							dataStr = Constants.FLG_WL + "," + index + ":" + CommonUtils.hexToDecimal(value);
						} else {
							System.out.println("[error] index_error:");
						}
						// publisher.push(Constants.TOPIC_DATA, sendStr);
						String sendStr = Constants.CLIENT_ID+","+ dataStr;
						System.out.println("[data] uart:" + sendStr);
						if(Constants.SEND_MQTT){
							publisher.push(Constants.TOPIC_DATA, sendStr);
						}
					} else {
						System.out.println("[error] readStr: length error");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
