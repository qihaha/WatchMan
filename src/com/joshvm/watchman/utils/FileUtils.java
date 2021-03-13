package com.joshvm.watchman.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 * 提供文件读写
 * /data/joshvm/user/
 *
 */
public class FileUtils {
	
	private static String configFileName = "config";
	private static final String gpioFilePrefix = "gpio";
	private static final String modelFile = "model";
	public static final int SLEEP_WL=2;
	public static final int SLEEP_ELE=4;
	public static final int SLEEP_GPS=6;
	public static final int SLEEP_GPIO=8;

	public static void main(String[] args) {

		System.out.println("     ==========     Start File Demo...");
		String fileName = "abc.txt";
		write(fileName,"nihao");
		System.out.println("read:"+read(fileName));
		write(fileName,"xiaomei");
		System.out.println("read:"+read(fileName));
			
	}
	
	public static int readConfig(int key){
		String configStr = read(configFileName);
		if(configStr.length()!=10){
			return 0;
		}else{
			return CommonUtils.hexToDecimal(configStr.substring(key,key+2));
		}
	}
	
	public static String readConfig(){
		return read(configFileName);
	}
	
	public static String writeConfig(String text){
		// 文件读写的时候是按位替换的，所以长度需要固定
		String confStr = text;
		if(text.length()>10){
			confStr=text.substring(0, 10);
		}
		write(configFileName,confStr);
		return readConfig();
	}
	
	public static String readGpioStatus(int index){
		String fileName = gpioFilePrefix+index;
		return read(fileName);
	}
	
	public static String readModel(){
		return read(modelFile);
	}
	
	public static String writeModel(String text){
		// 文件读写的时候是按位替换的，所以长度需要固定
		String modelStr = text;
		if(text.length()>4){
			modelStr=text.substring(0, 4);
		}
		write(modelFile,modelStr);
		return readModel();
	}
	
	
	public static String writeGpioStatus(int index, String status){
		String fileName = gpioFilePrefix+index;
		write(fileName,status);
		return read(fileName);
	}
	
	private static void write(String fileName,String message) {

		String fileURI = "/Phone/" + fileName;
		
		OutputStream outputStream = null;
		FileConnection fileConnection = null;

		try {

			// 创建FileConnection , 目前file:///Phone/根目录是固定的
			fileConnection = (FileConnection) Connector.open("file://"+fileURI);
			if (!fileConnection.exists()) {
				// 创建File
				fileConnection.create();
//				System.out.println("[debug] Creat files success....");
			}

			// 写入File
			outputStream = fileConnection.openOutputStream();
			outputStream.write(message.getBytes());
//			System.out.println("[debug] Write file success....");
			
			outputStream.close();
			outputStream = null;
			fileConnection.close();
			fileConnection = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String read(String fileName) {

		String fileURI = "/Phone/" + fileName;
		InputStream inputStream = null;
		FileConnection fileConnection = null;
		String text = "";

		try {
			// 读File
			fileConnection = (FileConnection) Connector.open("file://"+fileURI);
			
			if (!fileConnection.exists()) {
				fileConnection.close();
//				System.out.println("[debug] File reading error! Can't open file to read.");
				throw new IOException("Can't read file "+fileURI);
			}
			
			inputStream = fileConnection.openInputStream();
			byte[] buffer = new byte[256];
			int readLen = 0;
			while ((readLen = inputStream.read(buffer)) != -1) {
//				System.out.println("[debug] Read file success....");
//				System.out.println(new String(buffer, 0, readLen));
				text+=new String(buffer, 0, readLen);
			}
			
			inputStream.close();
			inputStream = null;
			fileConnection.close();
			fileConnection = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileConnection != null) {
				try {
					fileConnection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return text;
	}
	
	
	private static void delete(String fileName) {
		
		String fileURI = "/Phone/" + fileName;
		FileConnection fileConnection = null;
		try {
			fileConnection = null;	
			// 删除文件
			fileConnection = (FileConnection) Connector.open("file://"+fileURI);
			if (fileConnection.exists()) {

				fileConnection.delete();
//				System.out.println("[debug] Delete file success....");
			} else {
//				System.out.println("[debug] Error. Can't find file to delete");
			}
			fileConnection.close();
			fileConnection = null;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
