package com.joshvm.watchman.utils;

import java.io.IOException;

import org.joshvm.j2me.cellular.CellularDeviceInfo;

public class CommonUtils {

	public static boolean isSubscriberStoped = true;
	
	public static String getImei() {

		String imei = "";

		CellularDeviceInfo[] devices = CellularDeviceInfo.listCellularDevices();
		if (devices != null && devices.length > 0) {

			try {
				imei = devices[0].getIMEI();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imei;
	}

	public static int hexToDecimal(String hex) {
		int outcome = 0;
		for (int i = 0; i < hex.length(); i++) {
			char hexChar = hex.charAt(i);
			outcome = outcome * 16 + charToDecimal(hexChar);
		}
		return outcome;
	}

	private static int charToDecimal(char c) {
		if (c >= 'A' && c <= 'F')
			return 10 + c - 'A';
		else
			return c - '0';
	}
	
	public static String byte2hex(byte[] data) {
		return byte2hex(data, data.length);
	}

	public static String byte2hex(byte[] data, int len) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < len; ++i) {
			if (data[i] < 16 && data[i] > -1) {
				stringBuffer.append("0" + Integer.toHexString(data[i] & 255) + " ");
			} else {
				stringBuffer.append(Integer.toHexString(data[i] & 255) + " ");
			}
		}
		return stringBuffer.toString();
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
