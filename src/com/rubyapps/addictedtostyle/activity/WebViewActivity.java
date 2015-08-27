package com.rubyapps.addictedtostyle.activity;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.app.MyApplication;
import com.rubyapps.addictedtostyle.helper.DialogAboutBuilder;
import com.rubyapps.addictedtostyle.model.GridItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

public class WebViewActivity extends SherlockActivity {

	private WebView webView;
	private ActionBar actionBar;
	// Refresh menu item
	private MenuItem refreshMenuItem;

	private List<GridItem> itemsList;
	private NavigationListener navigationListener;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		Appodeal.show(this, Appodeal.BANNER_BOTTOM);
		url = getIntent().getStringExtra("url");
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (refreshMenuItem != null) {
					refreshMenuItem.setActionView(R.layout.action_progressbar);
					refreshMenuItem.expandActionView();
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (refreshMenuItem != null) {
					refreshMenuItem.collapseActionView();
					refreshMenuItem.setActionView(null);
				}
				super.onPageFinished(view, url);
			}
		});
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setUserAgentString(
				"Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");

		itemsList = ((MyApplication) this.getApplication()).getItemsList();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sherlock_spinner_dropdown_item,
				getItemNameList(itemsList));
		actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle("");
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			navigationListener = new NavigationListener();
			actionBar.setListNavigationCallbacks(adapter, navigationListener);
			adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
			actionBar.setSelectedNavigationItem(getPositionByURL(url));
		}
	}

	private class NavigationListener implements ActionBar.OnNavigationListener {

		private boolean loadUrl = true;

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if (url != null && getPositionByURL(url) == -1) {
				actionBar.setIcon(R.drawable.logo);
				webView.loadUrl(url);
				url = null;
				return false;
			} else {
				actionBar.setIcon(itemsList.get(itemPosition).getImageId());
			}
			if (loadUrl) {
				webView.loadUrl(itemsList.get(itemPosition).getUrl());
			} else {
				loadUrl = true;
			}
			return false;
		}

		void doNotLoadUrl() {
			this.loadUrl = false;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER);
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			WebBackForwardList forwardList = webView.copyBackForwardList();
			if (forwardList.getCurrentIndex() > 0) {
				String url = forwardList.getItemAtIndex(forwardList.getCurrentIndex() - 1).getUrl();
				/*
				 * http://www.fashionpolicenigeria.com/ redirects to
				 * http://www.fashionpolicenigeria.com/read-more/
				 */
				url = url.replace("/read-more", "");
				int position = getPositionByURL(url);
				if (position >= 0) {
					navigationListener.doNotLoadUrl();
					actionBar.setSelectedNavigationItem(position);
				} else {
					actionBar.setIcon(R.drawable.logo);
				}
			}
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
			break;
		case R.id.progress:
			webView.reload();
			break;
		case R.id.menu_about:
			(new DialogAboutBuilder()).buildAndShowDialog(this);
			break;
		case R.id.menu_settings:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivity(intentSettings);
			break;
		default:
			break;
		}
		return true;
	}

	private int getPositionByURL(String url) {
		for (GridItem item : itemsList) {
			if (item.getUrl().equals(url)) {
				return itemsList.indexOf(item);
			}
		}
		return -1;
	}

	private ArrayList<String> getItemNameList(List<GridItem> items) {
		ArrayList<String> result = new ArrayList<String>();
		for (GridItem item : items) {
			result.add(item.getName());
		}
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.web_view, menu);
		refreshMenuItem = menu.findItem(R.id.progress);
		return super.onCreateOptionsMenu(menu);
	}
}
