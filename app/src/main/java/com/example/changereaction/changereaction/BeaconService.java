package com.example.changereaction.changereaction;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Beacon情報収集・送信サービス
 * Created by ishiimao on 15/07/11.
 */
public class BeaconService extends Service implements LocationListener {

	//LocationManager
	private LocationManager locationManager;
	//Timer
	private Timer timer;
	//uuid
	private String uuid;
	//uuid取得時間
	private String nowtime;
	//経度
	private double longitude;
	//緯度
	private double latitude;
	//TODO URL
	private static final String url = "http://bluemixmoumuri.mybluemix.net/contact";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.i("beaconsample","BeaconSearchService_Create");
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
		Log.i("beaconsample","BeaconSearchService_Start");
		//super.onStartCommand(intent, flags, startId);
		//位置情報の取得設定
		if (locationManager != null) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,getInterval(),50, this);
		}
		//指定された間隔でbeaco情報を送信する
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//UUIDの取得
				uuid = getBeaconInfo();
				//時間の取得
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//"yyyy/MM/dd HH:mm:ss"
				nowtime = format.format(date);
				Log.i("beaconSample","uuid:"+ uuid);
				Log.i("beaconSample","latitude:"+ String.valueOf(latitude));
				Log.i("beaconSample","longtitude:"+ String.valueOf(longitude));
				Log.i("beaconSample","nowtime:"+ nowtime);
				//情報の送信
				if (uuid != null) {
					postData();
				}
			}
		}, 0, getInterval());


		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i("beaconsample","BeaconSearchService_Destroy");
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
		//Service開始時に仕込まれたtimerのキャンセル
		timer.cancel();
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

	/**
	 * beacon情報の送信処理
	 */
	private void postData() {
		try {
//			HttpURLConnection sampleCon = (HttpURLConnection) new URL("http://bluemixmoumuri.mybluemix.net/contact").openConnection();
//			sampleCon.setRequestMethod("GET");
//			sampleCon.connect();
//			int rs = sampleCon.getResponseCode();
//			Log.i("beaconsample","sampleGet...result:" + String.valueOf(rs));

			Log.i("beaconsample", "GetRequest");

			String param = "uuid=" + uuid + "&long=" + String.valueOf(longitude)
					+ "&lat=" + String.valueOf(latitude) + "&nowtime=" + nowtime.replace(" ","%20");
			Log.i("beaconsample","url:" + url + "?" + param);
			URL urlObj = new URL(url + "?" + param);
			HttpURLConnection hc = (HttpURLConnection) urlObj.openConnection();
			hc.setRequestProperty("uuid",uuid);
			hc.setRequestProperty("long",String.valueOf(longitude));
			hc.setRequestProperty("lat",String.valueOf(latitude));
			hc.setRequestProperty("nowtime",nowtime);
//			hc.setRequestProperty("Accept-Charset", "utf-8");
//			hc.setDoOutput(true);
			hc.setRequestMethod("GET");
			//resuestの送信
			hc.connect();
			int resultCd = hc.getResponseCode();
			Log.i("beaconsample","ResponseCode:" + String.valueOf(resultCd));

		} catch (MalformedURLException e) {
			Log.e("beaconsample", "Invalid URL format:" + url);

		} catch (IOException e) {
			Log.e("beaconsample", "Can't connect to" + url);
		}
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
