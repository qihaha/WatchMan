package com.joshvm.watchman.constant;

import com.joshvm.watchman.utils.CommonUtils;

public class Constants {

	public static final String SERVER_URI = "tcp://123.57.56.85:7710";
	public static final String CLIENT_ID = CommonUtils.getImei();
	public static final String USER_NAME = "ctdna";
	public static final String PASSWORD = "ctdna_test";
	public static final String TOPIC_DATA = "wm_data";
	public static final String TOPIC_ACTION = "wm_action";

	public static final String FLG_WL = "01";
	public static final String FLG_ELE = "02";
	public static final String FLG_GPS = "03";
	public static final String FLG_GPIO = "04";
	public static final String FLG_MODEL = "05";

	public static final String COM = "COM0";
	public static final int BAUDRATE = 9600;
	public static String CMD_CUSTOM = "";
	// 读有功功率，01 03 00 0e 00 01 E5 C9
	public static final byte[] ELE_BYTE_01 = new byte[] { (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x01, (byte) 0xe5, (byte) 0xc9 };
	// 距离传感器，读距离
	public static final byte[] WL_BYTE_02 = new byte[] { (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x24, (byte) 0x05 };
	public static final byte[] WL_BYTE_03 = new byte[] { (byte) 0x03, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x25, (byte) 0xd4 };

	public static final String GPIO_OFF = "00";
	public static final String GPIO_ON = "01";
	public static final int[] indexList = { 10, 11, 14, 15, 16, 17, 20, 23 };
	public static final int GPIO_JIN1 = 16;
	public static final int GPIO_JIN2 = 16;
	public static final int GPIO_BAO1 = 15;
	public static final int GPIO_BAO2 = 15;
	public static final int GPIO_XUNHUAN = 14;
	public static final int GPIO_CHU = 17;
	
	// 实验室接线
//	public static final int GPIO_BAO1 = 14; // 回流泵
//	public static final int GPIO_BAO2 = 15; // 风机 
//	public static final int GPIO_XUNHUAN = 16;  //进水
//	public static final int GPIO_CHU = 17;  //出水

	public static final int MODEL_SWITCH_TIME = 24 * 60 * 60;
	public static final String MODEL_DEFULT = "0000";
	public static final String MODEL_SINGLE = "01";
	public static final String MODEL_DOUBLE = "02";
	public static final String MODEL_XIAOHUA = "01";
	public static final String MODEL_TUODAN = "02";
	public static final String MODEL_STOP = "03";

	public static final boolean SEND_MQTT = true;
	public static final boolean DEBUG = true;
	public static final boolean GPIO_STATUS = false;// 真状态检测
	

}
