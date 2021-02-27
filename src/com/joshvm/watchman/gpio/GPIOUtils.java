package com.joshvm.watchman.gpio;

import org.joshvm.j2me.dio.DeviceManager;
import org.joshvm.j2me.dio.gpio.GPIOPin;
import org.joshvm.j2me.dio.gpio.GPIOPinConfig;

public class GPIOUtils {

	/**
	 * 根据使用的设备设置相对应的值，
	 * 1:使用JOSH_MEGA_8300  GPIO_1=4,GPIO_2=27,GPIO_3=29,PWM/IO=25  ,现以GPIO_3=29为例
	 * 2:使用JOSH_MEGA_ESP32/ESP-LyraT-Mini
	 * 3:使用 ESP-WROVER-KIT
	 */

	static GPIOPinConfig cfgOutput;
	static GPIOPin outputGpio;

	public static void main(String[] args) {
		System.out.println("      ===============      Start GPIO Demo...");
		int JOSH_GPIO = 14;
		try {
			cfgOutput = new GPIOPinConfig(GPIOPinConfig.UNASSIGNED, JOSH_GPIO,
					GPIOPinConfig.DIR_OUTPUT_ONLY, GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN, GPIOPinConfig.TRIGGER_NONE,
					false);
			outputGpio = (GPIOPin) DeviceManager.open(cfgOutput, DeviceManager.EXCLUSIVE);

			while (true) {
				Thread.sleep(5000);
				outputGpio.setValue(true);
				Thread.sleep(3000);
				outputGpio.setValue(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static boolean gpioSwitch(int index, boolean status) {
		boolean relStatus = status;
		try {
			cfgOutput = new GPIOPinConfig(GPIOPinConfig.UNASSIGNED, index,
					GPIOPinConfig.DIR_OUTPUT_ONLY, GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN, GPIOPinConfig.TRIGGER_NONE,
					false);
			outputGpio = (GPIOPin) DeviceManager.open(cfgOutput, DeviceManager.EXCLUSIVE);
			outputGpio.setValue(status);
			relStatus = outputGpio.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relStatus;
	}
}