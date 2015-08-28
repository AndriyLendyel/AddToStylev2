package com.rubyapps.addictedtostyle.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;
import com.rubyapps.addictedtostyle.activity.WebViewActivity;
import com.rubyapps.addictedtostyle.app.AppConfig;
import com.rubyapps.addictedtostyle.app.MyApplication;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
	private final String TAG = CustomPushReceiver.class.getSimpleName();

	@Override
	protected void onPushReceive(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences(AppConfig.SETTINGS, 0);
		if (settings.getBoolean(AppConfig.NOTIFICATION, true)) {
			super.onPushReceive(context, intent);
		}
	}

	@Override
	protected void onPushOpen(Context context, Intent intent) {
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String url = json.has("url") ? json.getString("url") : null;
			intent.putExtra("url", url);
		} catch (JSONException e) {
			Log.e(TAG, "Push message json exception: " + e.getMessage());
		} finally {
			super.onPushOpen(context, intent);
		}
	}

	@Override
	protected Class<? extends Activity> getActivity(Context context, Intent intent) {
		Class<? extends Activity> activity;
		MyApplication application = MyApplication.class.cast(context.getApplicationContext());
		if (intent.getStringExtra("url") != null && application.getPositionByURL(intent.getStringExtra("url")) >= 0) {
			activity = WebViewActivity.class;
		} else {
			activity = super.getActivity(context, intent);
		}

		return activity;
	}

	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Notification notification = super.getNotification(context, intent);
		SharedPreferences settings = context.getSharedPreferences(AppConfig.SETTINGS, 0);
		notification.defaults = 0;
		if (settings.getBoolean(AppConfig.NOTIFICATION_SOUND, true)) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if (settings.getBoolean(AppConfig.NOTIFICATION_VIBRO, true)) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		if (settings.getBoolean(AppConfig.NOTIFICATION_LIGHT, true)) {
			notification.defaults |= Notification.DEFAULT_LIGHTS;
		}
		return notification;
	}

}