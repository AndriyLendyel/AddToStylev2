package com.rubyapps.addictedtostyle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.app.AppConfig;

public class SettingsActivity extends SherlockActivity {

	CheckBox notification;
	CheckBox notificationSound;
	CheckBox notificationVibro;
	CheckBox notificationLight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		notification = (CheckBox) findViewById(R.id.checkNotification);
		notificationSound = (CheckBox) findViewById(R.id.checkSound);
		notificationVibro = (CheckBox) findViewById(R.id.checkVibro);
		notificationLight = (CheckBox) findViewById(R.id.checkLight);
		SharedPreferences settings = getSharedPreferences(AppConfig.SETTINGS, 0);
		notification.setChecked(settings.getBoolean(AppConfig.NOTIFICATION, true));
		notificationSound.setChecked(settings.getBoolean(AppConfig.NOTIFICATION_SOUND, true));
		notificationVibro.setChecked(settings.getBoolean(AppConfig.NOTIFICATION_VIBRO, true));
		notificationLight.setChecked(settings.getBoolean(AppConfig.NOTIFICATION_LIGHT, true));
		notification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				enableCheckboxes(isChecked);
			}
		});
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		Appodeal.show(this, Appodeal.BANNER_BOTTOM);
		enableCheckboxes(notification.isChecked());
	}

	private void enableCheckboxes(boolean isChecked) {
		if (isChecked) {
			notificationLight.setEnabled(true);
			notificationSound.setEnabled(true);
			notificationVibro.setEnabled(true);
		} else {
			notificationLight.setEnabled(false);
			notificationSound.setEnabled(false);
			notificationVibro.setEnabled(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(AppConfig.SETTINGS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(AppConfig.NOTIFICATION, notification.isChecked());
		editor.putBoolean(AppConfig.NOTIFICATION_SOUND, notificationSound.isChecked());
		editor.putBoolean(AppConfig.NOTIFICATION_VIBRO, notificationVibro.isChecked());
		editor.putBoolean(AppConfig.NOTIFICATION_LIGHT, notificationLight.isChecked());
		editor.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
