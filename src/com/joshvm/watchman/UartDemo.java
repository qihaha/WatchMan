package com.joshvm.watchman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.bouncycastle.util.Arrays;

/**
 * UartDemo
 * <p>
 * 实现串口通信。
 * <p>
 * 提供：连接，读数据，发数据，关闭功能
 * <p>
 * 实现功能：每1s发送一段数据；不断接收数据
 * 
 * @author Administrator
 *
 */
public class UartDemo {

	// 根据硬件及接线设定串口的com口
	private static String com = "COM0";
	// 波特率
	private static int baudrate = 9600;

	private static StreamConnection streamConnection;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args) {

		System.out.println(" ============ Start1 Uart Demo...");

		String host = "comm:" + com + ";baudrate=" + baudrate;

		try {

			// 建立连接
			streamConnection = (StreamConnection) Connector.open(host);
			inputStream = streamConnection.openInputStream();
			outputStream = streamConnection.openOutputStream();
//			final byte[] b = new byte[]{0x00,0x04,0x00,0x07,0x00,0x01,0x81,0xDA};

			// 启动子线程，每1s发送数据
			new Thread(new Runnable() {

				public void run() {

					while (true) {
						try {

							byte cmd_query[] = {1, 3, 1, 2, 0, 1, 36, 54};
//							outputStream.write(hexToByteArray("0103010200012436"));
							outputStream.write(cmd_query,0,8);
							outputStream.flush();
//							System.out.println("send:{0x01, 0x03,0x01,0x02,0x00,0x01,0x24,0x36}");
							Thread.sleep(3000);
							

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
				String t="";
				for(int i=0;i<buffer.length;i++) {
					t+=(int)buffer[i]+",";
				}
				String message = new String(buffer, 0, len);
				System.out.println("get message1 : " + t);
				System.out.println("get message2 : " + bytesToHexString(buffer));
				System.out.println("get message3 : " + message);
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	/**
     * 将字节数字转换为16进制字符串
     * @param bytes
     * @return
     */
//	private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

//    private static String bytesToHexString(byte[] bytes) {
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for(int i=0;i<bytes.length;i++) { // 利用位运算进行转换，可以看作方法一的变种
//        	byte b = bytes[i];
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
// 
//        return new String(buf);
//    }
 
    /**
     * 将十六进制字符串转换字节数组
     * @param str
     * @return
     */
//    private static byte[] hexStringToBytes(String str) {
//        if(str == null || str.trim().equals("")) {
//            return new byte[0];
//        }
// 
//        byte[] bytes = new byte[str.length() / 2];
//        for(int i = 0; i < str.length() / 2; i++) {
//            String subStr = str.substring(i * 2, i * 2 + 2);
//            bytes[i] = (byte) Integer.parseInt(subStr, 16);
//        }
// 
//        return bytes;
//    }
    
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
    
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }
    
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    
    private static String printHexString(byte[] b) {

        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sbf.append(hex.toUpperCase() + "  ");
        }
        return sbf.toString().trim();
    }
    

}
