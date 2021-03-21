package com.joshvm.watchman.core;

import java.io.IOException;
import java.io.OutputStream;

import com.joshvm.watchman.constant.Constants;
import com.joshvm.watchman.utils.FileUtils;

public class UartWriteThread extends Thread{
	
	private OutputStream outputStream;
	public UartWriteThread(OutputStream outputStream){
		this.outputStream=outputStream;
	}
	

	public void run() {
		while (true) {
			try {
				int sleepSecond = FileUtils.readConfig(FileUtils.SLEEP_UART);
				outputStream.write(Constants.ELE_BYTE_01, 0, Constants.ELE_BYTE_01.length);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
				outputStream.write(Constants.WL_BYTE_02, 0, Constants.WL_BYTE_02.length);
				Thread.sleep((sleepSecond == 0 ? 1 : sleepSecond) * 1000);
				outputStream.write(Constants.WL_BYTE_03, 0, Constants.WL_BYTE_03.length);
				Thread.sleep((sleepSecond == 0? 1 : sleepSecond) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
