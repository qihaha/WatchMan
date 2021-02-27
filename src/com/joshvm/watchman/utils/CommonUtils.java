package com.joshvm.watchman.utils;

import java.io.IOException;

import org.joshvm.j2me.cellular.CellularDeviceInfo;

public class CommonUtils {

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
	

}
