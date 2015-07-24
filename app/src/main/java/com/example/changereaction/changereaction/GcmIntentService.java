package com.example.changereaction.changereaction;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ishiimao on 15/07/23.
 */
public class GcmIntentService extends IntentService {

	private Handler handler = new Handler();


	public GcmIntentService() {
		super(GcmIntentService.class.getName());
	}

	public GcmIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("beaconsample","GcmIntentService_Handle");
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			Log.d("beaconsample",extras.toString());

		}
		GcmBroadCastReceiver.completeWakefulIntent(intent);
		Object obj = extras.get("com.xtify.sdk.NOTIFICATION_CONTENT");
		final String message = obj != null ? String.valueOf(obj) : null;
		if (message != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast toast =  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0,0);
					toast.show();
				}
			});
		}
	}
}
