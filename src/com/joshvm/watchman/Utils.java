package com.joshvm.watchman;

import java.io.IOException;

import org.joshvm.j2me.cellular.CellularDeviceInfo;

public class Utils {

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
	
}
