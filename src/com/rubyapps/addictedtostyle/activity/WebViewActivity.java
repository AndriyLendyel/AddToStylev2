package com.rubyapps.addictedtostyle.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.app.MyApplication;
import com.rubyapps.addictedtostyle.model.GridItem;

public class WebViewActivity extends SherlockActivity {

	private WebView webView;
	private ActionBar actionBar;
	// Refresh menu item
	private MenuItem refreshMenuItem;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		Appodeal.show(this, Appodeal.BANNER_BOTTOM);
		final String url = getIntent().getStringExtra("url");
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (Build.VERSION.SDK_INT >= 11) {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		CookieSyncManager.createInstance(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setLightTouchEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		if (Build.VERSION.SDK_INT > 7) {
			webView.getSettings().setPluginState(
					WebSettings.PluginState.ON_DEMAND);
		}
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
		webView.clearCache(true);
		webView.loadUrl(getIntent().getStringExtra("url"));

		final ArrayList<GridItem> itemsList = (ArrayList<GridItem>) ((MyApplication) this
				.getApplication()).getItemsList();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.sherlock_spinner_dropdown_item,
				getItemNameList(itemsList));
		actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle("");
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		}
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				if (itemPosition != -1) {
					actionBar.setIcon(itemsList.get(itemPosition).getImageId());
					webView.loadUrl(itemsList.get(itemPosition).getUrl());
					webView.invalidate();
				} else {
					actionBar.setIcon(R.drawable.logo);
					webView.loadUrl(url);
				}
				return false;
			}
		};
		actionBar.setListNavigationCallbacks(adapter, navigationListener);
		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		actionBar.setSelectedNavigationItem(getPositionByURL(itemsList, url));
	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER);
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			// TODO back
			break;
		case R.id.progress:
			// refresh
			refreshMenuItem = item;
			// load the data from server
			new SyncData().execute();
			break;
		case R.id.menu_about:
			break;
		case R.id.menu_settings:
			break;
		default:
			break;
		}
		return true;
	}

	public int getPositionByURL(ArrayList<GridItem> items, String url) {
		for (GridItem item : items) {
			if (item.getUrl().equals(url)) {
				return items.indexOf(item);
			}
		}
		return -1;
	}

	public ArrayList<String> getItemNameList(ArrayList<GridItem> items) {
		ArrayList<String> result = new ArrayList<String>();
		for (GridItem item : items) {
			result.add(item.getName());
		}
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.web_view, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private class SyncData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			refreshMenuItem.setActionView(R.layout.action_progressbar);
			refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			refreshMenuItem.setActionView(null);
		}
	};
}
