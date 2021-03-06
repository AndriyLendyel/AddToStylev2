package com.rubyapps.addictedtostyle.activity;

import java.util.List;

import org.jsoup.Jsoup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.actionbarsherlock.widget.ShareActionProvider.OnShareTargetSelectedListener;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.adapter.MyGridViewAdapter;
import com.rubyapps.addictedtostyle.app.AppConfig;
import com.rubyapps.addictedtostyle.app.MyApplication;
import com.rubyapps.addictedtostyle.helper.DialogBuilder;
import com.rubyapps.addictedtostyle.helper.ParseUtils;
import com.rubyapps.addictedtostyle.model.GridItem;

public class MainActivity extends SherlockActivity {

	private static final String COUNT_TEXT = "count";
	private static final int COUNT = 21;
	private ShareActionProvider mShareActionProvider;
	private GridView gridView;
	final Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ParseUtils.verifyParseConfiguration(this);
		gridView = (GridView) findViewById(R.id.gridView);
		final List<GridItem> itemsList = ((MyApplication) this.getApplication()).getItemsList();
		MyGridViewAdapter adapter = new MyGridViewAdapter(this, R.layout.grid_item, itemsList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
				intent.putExtra("url", itemsList.get(arg2).getUrl());
				startActivity(intent);
			}
		});
		Appodeal.disableNetwork((Context) this, "facebook");
		Appodeal.setBannerViewId(R.id.appodealBannerView);
		Appodeal.initialize(this, AppConfig.AD_APP_KEY, Appodeal.INTERSTITIAL | Appodeal.BANNER);
		Appodeal.setInterstitialCallbacks(interstitialListener);
		Appodeal.show(this, Appodeal.BANNER_VIEW);
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);	
			}
		}, 30000);
		checkForUpdates();

	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences prefs = getSharedPreferences(AppConfig.MY_PREFS_NAME, MODE_PRIVATE);
		if (!prefs.getBoolean(AppConfig.SURE_PRESSED, false)) {
			int countVisited = prefs.getInt(COUNT_TEXT, 0);
			SharedPreferences.Editor editor = prefs.edit();
			if (countVisited < COUNT) {
				editor.putInt(COUNT_TEXT, ++countVisited);
				editor.commit();
			} else {
				editor.putInt(COUNT_TEXT, 0);
				editor.commit();
				handler.postDelayed(showDialog, 70000);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		String message = intent.getStringExtra("message");
		intent.removeExtra("url");
		intent.removeExtra("message");
		if ((url != null && !url.isEmpty()) || message != null) {
			Intent intentWeb = new Intent(MainActivity.this, WebViewActivity.class);
			intentWeb.putExtra("url", url);
			intentWeb.putExtra("message", message);
			intentWeb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intentWeb);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(sendData);
		handler.removeCallbacks(showDialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
		Intent intent = getDefaultShareIntent();
		if (intent != null) {
			mShareActionProvider.setShareIntent(intent);
		}
		mShareActionProvider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener() {

			@Override
			public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
				// start activity ourself to prevent search history
				MainActivity.this.startActivity(intent);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	private Intent getDefaultShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		String shareBody = "Check out Addicted to Style on Google Play! https://play.google.com/store/apps/details?id=";
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Addicted to style");
		intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + getPackageName());
		intent.setType("text/plain");
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			(new DialogBuilder()).buildAndShowAboutDialog(this);
			break;
		}
		return true;
	}

	public InterstitialCallbacks interstitialListener = new InterstitialCallbacks() {

		@Override
		public void onInterstitialShown() {
		}

		@Override
		public void onInterstitialLoaded(boolean arg0) {
		}

		@Override
		public void onInterstitialFailedToLoad() {
		}

		@Override
		public void onInterstitialClosed() {
			handler.post(sendData);
		}

		@Override
		public void onInterstitialClicked() {
			handler.post(sendData);
		}
	};

	private final Runnable sendData = new Runnable() {
		public void run() {
			try {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
					}
				}, 8*60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private final Runnable showDialog = new Runnable() {
		public void run() {
			(new DialogBuilder()).buildAndShowRateUsDialog(MainActivity.this);
		}
	};

	private void checkForUpdates() {
		new CheckUpdates().execute();
	}
	
	private class CheckUpdates extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	try {
				String curVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
				String newVersion = curVersion;
				newVersion = Jsoup
						.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
						.timeout(30000)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.referrer("http://www.google.com").get().select("div[itemprop=softwareVersion]").first().ownText();
				return (Double.valueOf(curVersion) < Double.valueOf(newVersion)) ? true : false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	    }
	    
	    @Override
	    protected void onPostExecute(Boolean isNewVersionAvailable) {
	    	if (isNewVersionAvailable){
	    		new DialogBuilder().buildAndShowUpdateDialog(MainActivity.this);
	    	}
	    }
	}
}