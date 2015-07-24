package com.example.changereaction.changereaction;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.push.IBMPush;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends Activity {

    /** Google Cloud Messagingオブジェクト */
    private GoogleCloudMessaging gcm;
    /** コンテキスト */
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getBeaconBtn = (Button) this.findViewById(R.id.getBeaconBtn);
        //Intent生成
        final Intent intent = new Intent(MainActivity.this, BeaconService.class);
        //Service 開始ボタン押下
        getBeaconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //サービス開始

                startService(intent);

            }
        });
        //Service 停止ボタン押下
        Button stopBeaconBtn = (Button) this.findViewById(R.id.stopBeaconBtn);
        stopBeaconBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        //GCM
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(context);
        registerInBackground();
        //１回登録すればよいので、現時点でコメントアウト。本来は正しくハンドリングすべきだが。
        //init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regid = gcm.register("138680713473");
                    Log.d("beaconsample","regId:" + regid);
                    msg = "Device registered, registration ID=" + regid;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    private void init() {
        IBMBluemix.initialize(this,
                "832c7a06-98af-4aa5-a098-cfb7adc9835d",
                "5e30eb245eecbc70784c28801c9373b24dcdab69",
                "http://bluemixmoumuri.mybluemix.net");
        IBMData.initializeService();

        IBMPush.initializeService();
        IBMPush push = IBMPush.getService();
        push.register("APA91bH66YZ5q-XPw9GSZ0zAfqynZSFx0fZq_Gb6a_G0BS1i5t5JFwIoGLlM6Q-PRxp08PKPSzy_uDTH9hu87mNO0j2G1yoBxL6HYY0-oFLxacTwMwAYm17_RGWRZKZmx8bYJbgnX-f8rHgyxfEM_UDbCtSaO8qBMw",
                "hogehoge").continueWith(new Continuation<String, Void>() {

            @Override
            public Void then(Task<String> task) throws Exception {
                if (task.isCancelled()) {
                    Log.e("beaconsample", "Exception : Task " + task.toString() + " was cancelled.");
                } else if (task.isFaulted()) {
                    Log.e("beaconsample", "Exception : " + task.getError().getMessage());
                } else {
                    Log.d("beaconsample", "Device Successfully Registered");
                }
                return null;
            }
        });

    }

}
