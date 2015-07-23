package com.example.changereaction.changereaction;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ishiimao on 15/07/11.
 */
public class BeaconService extends Service implements LocationListener {

	//LocationManager
	LocationManager locationManager;
	//Timer
	Timer timer;
	//uuid
	String uuid;
	//uuid取得時間
	String nowtime;
	//経度
	double longitude;
	//緯度
	double latitude;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.d("changereaction","BeaconSearchService_Start");
		Toast.makeText(this, "BeaconSearchService_Start", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 次回サービス起動までの間隔を定義
	 * @return interval
	 */
	protected long getInterval() {
		//3分
		return 1000 * 10;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//super.onStartCommand(intent, flags, startId);
		//位置情報の取得設定
		if (locationManager != null) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,getInterval(),50, this);
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//UUIDの取得
				uuid = getBeaconInfo();
				//時間の取得
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				nowtime = format.format(date);
				Log.i("beaconSample","uuid:"+ uuid);
				Log.i("beaconSample","latitude:"+ String.valueOf(latitude));
				Log.i("beaconSample","longtitude:"+ String.valueOf(longitude));
				Log.i("beaconSample","nowtime:"+ nowtime);


			}
		}, 0, getInterval());

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

	/**
	 * UUIDの取得
	 * @return UUID
	 */
	private String getBeaconInfo() {
		//bluetootマネージャー起動
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		BeaconScanCallback mLeScanCallback = new BeaconScanCallback();
		//スキャン開始
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		//スキャン停止
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		return mLeScanCallback.getUuid();
	}


	@Override
	public void onLocationChanged(Location location) {

		longitude = location.getLongitude();
		latitude = location.getLatitude();
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
}
