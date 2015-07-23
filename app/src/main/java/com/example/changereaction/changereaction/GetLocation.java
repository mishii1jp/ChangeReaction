package com.example.changereaction.changereaction;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by ishiimao on 15/07/23.
 */
public class GetLocation implements LocationListener {

	//経度
	private double latitude;
	//緯度
	private double longitude;

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * 経度取得
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 緯度取得
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}
}
