package com.example.changereaction.changereaction;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ishiimao on 15/07/23.
 */
public class GcmIntentService extends IntentService {

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
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				Log.d("beaconsample",extras.toString());
				extras.toString();
			}
		}
		GcmBroadCastReceiver.completeWakefulIntent(intent);
	}
}
