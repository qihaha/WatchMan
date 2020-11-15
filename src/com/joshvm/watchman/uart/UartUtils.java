package com.joshvm.watchman.uart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.joshvm.watchman.WatchMan;
import com.joshvm.watchman.mqtt.Publisher;

/**
 * UartUtils
 * <p>
 * 实现串口通信。
 * 
 * @author Administrator
 *
 */
public class UartUtils {
	

	private StreamConnection streamConnection;
	public InputStream inputStream;
	public OutputStream outputStream;

	public UartUtils(String com, int baudrate) {
		// 建立连接
		String host = "comm:" + com + ";baudrate=" + baudrate;
		try {
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readData() {
		String message = "";
		try {
			int len = 0;
			byte[] buffer = new byte[7];
			while ((len = inputStream.read(buffer)) != -1) {
				message = bytesToHexString(buffer);
				System.out.println("get:"+message);
				Publisher publisher = new Publisher(WatchMan.SERVER_URI,WatchMan.CLIENT_ID,WatchMan.USER_NAME,WatchMan.PASSWORD);
				publisher.push(WatchMan.TOPIC, message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public boolean writeData(String message) {
		try {
			outputStream.write(hexToByteArray(message));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static byte[] hexToByteArray(String inHex) {
	    int hexlen = inHex.length();
	    byte[] result;
	    if (hexlen % 2 == 1) {
	      // 奇数
	      hexlen++;
	      result = new byte[(hexlen / 2)];
	      inHex = "0" + inHex;
	    } else {
	      // 偶数
	      result = new byte[(hexlen / 2)];
	    }
	    int j = 0;
	    for (int i = 0; i < hexlen; i += 2) {
	      result[j] = hexToByte(inHex.substring(i, i + 2));
	      j++;
	    }
	    return result;
	  }
	
	  private static byte hexToByte(String inHex) {
		    return (byte) Integer.parseInt(inHex, 16);
		  }

	  public static String bytesToHexString(byte[] bArray) {
		    StringBuffer sb = new StringBuffer(bArray.length);
		    String sTemp;
		    for (int i = 0; i < bArray.length; i++) {
		      sTemp = Integer.toHexString(0xFF & bArray[i]);
		      if (sTemp.length() < 2){
		        sb.append(0);
		      }
		      sb.append(sTemp.toUpperCase());
		    }
		    return sb.toString();
		  }
}
