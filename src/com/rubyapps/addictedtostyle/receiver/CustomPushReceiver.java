package com.rubyapps.addictedtostyle.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
	private final String TAG = CustomPushReceiver.class.getSimpleName();

	@Override
	protected void onPushOpen(Context context, Intent intent) {
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String url = json.has("url") ? json.getString("url") : null;
			intent.putExtra("url", url);
			intent.putExtra("message", json.getString("alert"));
		} catch (JSONException e) {
			Log.e(TAG, "Push message json exception: " + e.getMessage());
		} finally {
			super.onPushOpen(context, intent);
		}
	}

}