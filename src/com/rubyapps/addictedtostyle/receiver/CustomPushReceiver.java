package com.rubyapps.addictedtostyle.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParsePushBroadcastReceiver;
import com.rubyapps.addictedtostyle.activity.WebViewActivity;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
	private final String TAG = CustomPushReceiver.class.getSimpleName();

	@Override
	protected void onPushReceive(Context context, Intent intent) {
		super.onPushReceive(context, intent);
	}

	@Override
	protected void onPushOpen(Context context, Intent intent) {
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String url = json.has("url") ? json.getString("url") : null;
			intent.putExtra("url", url);
		} catch (JSONException e) {
			Log.e(TAG, "Push message json exception: " + e.getMessage());
		}
		super.onPushOpen(context, intent);
	}

	@Override
	protected Class<? extends Activity> getActivity(Context context, Intent intent) {
		Class<? extends Activity> activity;
		if (intent.getStringExtra("url") != null) {
			activity = WebViewActivity.class;
		} else {
			activity = super.getActivity(context, intent);
		}

		return activity;
	}

	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Notification notification = super.getNotification(context, intent);
		//TODO add settings 
		//notification.defaults = 0;
		notification.sound = null;
		return notification;
	}

}