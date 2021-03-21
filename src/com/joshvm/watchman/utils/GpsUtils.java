package com.joshvm.watchman.utils;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

public class GpsUtils {
	

	public static void main(String[] args) {
		testA();
		testB();
	}
	
	public static String getLocation() {
		String locationStr = "";
		try {
			LocationProvider gpsProvider = LocationProvider.getInstance(null);
			Location location = gpsProvider.getLocation(3); // 这个时间阻塞等待，时间太长的话返回结果太慢
			locationStr = getLocationString(location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationStr;
	}
	
	private static String getLocationString(Location location) {
		if (location == null) {
			return "null";
		}
		System.out.println("dddd:"+location.toString());
		QualifiedCoordinates coodinate = location.getQualifiedCoordinates();
		if (coodinate == null) {
			return "null";
		}
		StringBuffer buf = new StringBuffer();
		buf.append("latitude:" + coodinate.getLatitude()).append(",");
		buf.append("longitude:" + coodinate.getLongitude());
		return buf.toString();
	}

	private static void testA() {
		System.out.println(">>>>>>>>>>>>>>>");
		try {
			LocationProvider gpsProvider = LocationProvider.getInstance(null);
			gpsProvider.setLocationListener(new LocationListener() {
				public void locationUpdated(LocationProvider provider, Location location) {
					System.out.println("[recv location]:" + getLocationString(location));
				}

				public void providerStateChanged(LocationProvider provider, int newState) {
				}
			}, 10, -1, -1);

			// wait some time
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gpsProvider.setLocationListener(null, -1, -1, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<<<<<<<<<<<<<<");
	}

	private static void testB() {
		System.out.println(">>>>>>>>>>>>>>>");
		try {
			LocationProvider gpsProvider = LocationProvider.getInstance(null);
			Location location = gpsProvider.getLocation(60); // timeout: 60 secs
			System.out.println("[get location]:" + getLocationString(location));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<<<<<<<<<<<<<<<");
	}
}
