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
	public static final String FLG_GPIO = "04";
	
	public static final String COM = "COM0";
	public static final int BAUDRATE = 9600;
	public static String CMD_CUSTOM = "";
	public static final byte[] WL_BYTE_01 = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; // "0103010200012436"
	public static final byte[] WL_BYTE_02 = new byte[] { 1, 3, 1, 2, 0, 1, 36, 54 }; // "0103010200012436"
	
	public static final String GPIO_OFF = "00";
	public static final String GPIO_ON = "01";
	public static final int[] indexList = { 10, 11, 14, 15, 16, 17, 20, 23 };
	public static final int GPIO_JIN1 = 10;
	public static final int GPIO_JIN2 = 11;
	public static final int GPIO_BAO1 = 14;
	public static final int GPIO_BAO2 = 15;
	public static final int GPIO_XUNHUAN = 16;
	public static final int GPIO_CHU = 17;
	
	public static final int MODEL_SWITCH_TIME=24*60*60;
	public static final String MODEL_DEFULT="0000";
	public static final String MODEL_SINGLE="01";
	public static final String MODEL_DOUBLE="02";
	public static final String MODEL_STOP="01";
	public static final String MODEL_XIAOHUA="02";
	public static final String MODEL_TUODAN="03";


}
