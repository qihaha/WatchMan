package com.joshvm.watchman.constant;

import com.joshvm.watchman.utils.CommonUtils;

public class Constants {
	
	public static final String SERVER_URI = "tcp://39.98.64.167:61613";
	public static final String CLIENT_ID = CommonUtils.getImei();
	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "password";
	public static final String TOPIC_DATA = "wm_data";
	public static final String TOPIC_ACTION = "wm_action";
	
	public static final String FLG_WL = "01";
	public static final String FLG_ELE = "02";
	public static final String FLG_GPS = "03";
	
	public static final String COM = "COM0";
	public static final int BAUDRATE = 9600;
	public static final byte[] WL_BYTE_01 = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; // "0103010200012436"
	public static final byte[] WL_BYTE_02 = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; // "0103010200012436"


}
