package com.joshvm.watchman.demo;
import org.joshvm.j2me.dio.DeviceManager;
import org.joshvm.j2me.dio.gpio.GPIOPin;
import org.joshvm.j2me.dio.gpio.GPIOPinConfig;

public class GPIODemo0329 {
	
	static int pinNumber1 = 14;
	static int pinNumber2 = 14;
	
	static GPIOPinConfig outputGPIOPinConfig1;
	static GPIOPin outputGPIOPin1;
	
	static GPIOPinConfig inputGPIOPinConfig1;
	static GPIOPin inputGPIOPin1;
	
	public static void main(String[] args) {
		
		try {
			outputGPIOPinConfig1 = new GPIOPinConfig(GPIOPinConfig.UNASSIGNED, pinNumber1, 
					GPIOPinConfig.DIR_OUTPUT_ONLY, GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, 
					GPIOPinConfig.TRIGGER_NONE, false);
			outputGPIOPin1 = (GPIOPin) DeviceManager.open(outputGPIOPinConfig1, DeviceManager.EXCLUSIVE);
			
			inputGPIOPinConfig1 = new GPIOPinConfig(GPIOPinConfig.UNASSIGNED, pinNumber2, 
					GPIOPinConfig.DIR_INPUT_ONLY, GPIOPinConfig.MODE_INPUT_PULL_UP, 
					GPIOPinConfig.TRIGGER_LOW_LEVEL, true);
			inputGPIOPin1 = (GPIOPin) DeviceManager.open(inputGPIOPinConfig1, DeviceManager.EXCLUSIVE);
			
			new Thread(new Runnable() {

				public void run() {
					try {
						while(true) {
							
							Thread.sleep(1 * 1000);
							
//							System.out.println(pinNumber2 + ":" + inputGPIOPin1.getValue());
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				
			}).start();
			
			while(true) {
				System.out.println("outputGPIOPin1.setValue:true" );
				outputGPIOPin1.setValue(true);
				System.out.println("outputGPIOPin1" +outputGPIOPin1.getValue());
				Thread.sleep(5 * 1000);
				System.out.println("outputGPIOPin1.setValue:false");
				outputGPIOPin1.setValue(false);
				System.out.println("outputGPIOPin1" +outputGPIOPin1.getValue());
				
				Thread.sleep(5 * 1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
