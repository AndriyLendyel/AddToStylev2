package com.rubyapps.addictedtostyle.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.app.MyApplication;
import com.rubyapps.addictedtostyle.helper.DialogBuilder;
import com.rubyapps.addictedtostyle.model.GridItem;

public class WebViewActivity extends SherlockActivity {

	private WebView webView;
	private ActionBar actionBar;
	// Refresh menu item
	private MenuItem refreshMenuItem;

	private List<GridItem> itemsList = new ArrayList<GridItem>();
	private NavigationListener navigationListener;
	private String url;
	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		Appodeal.show(this, Appodeal.BANNER_VIEW);
		url = getIntent().getStringExtra("url");
		getIntent().removeExtra("url");
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
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

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Uri.parse(url).getScheme().equals("market")) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						Activity host = (Activity) view.getContext();
						host.startActivity(intent);
						return true;
					} catch (ActivityNotFoundException e) {
						Uri uri = Uri.parse(url);
						view.loadUrl("http://play.google.com/store/apps/" + uri.getHost() + "?" + uri.getQuery());
						return false;
					}
				}
				return false;
			}
		});
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");

		myApplication = (MyApplication) this.getApplication();
		itemsList.add(new GridItem(R.drawable.logo, "Addicted to Style", ""));
		itemsList.addAll(myApplication.getItemsList());

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
			int positionByURL = getPositionByURL(url);
			actionBar.setSelectedNavigationItem(positionByURL);
		}
	}

	private class NavigationListener implements ActionBar.OnNavigationListener {

		private boolean loadUrl = true;

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			actionBar.setIcon(itemsList.get(itemPosition).getImageId());

			if (!loadUrl) {
				loadUrl = true;
				return true;
			}

			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			if (itemPosition == 0) {
				if (url != null && !url.isEmpty()) {
					webView.loadUrl(url);
					getIntent().removeExtra("message");
					url = null;
				} else {

					String message = getIntent().getStringExtra("message");
					if (message != null && !message.isEmpty()) {
						webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
						webView.loadData("<h1>" + message + "</h1>", "text/html", "utf-8");
					} else {
						finish();
						Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
						startActivity(intent);
					}
				}
			} else {
				if (itemPosition == itemsList.size() - 1) {
					webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
				}
				actionBar.setIcon(itemsList.get(itemPosition).getImageId());
				if (url != null && !url.isEmpty()) {
					webView.loadUrl(url);
					getIntent().removeExtra("message");
					url = null;
				} else {
					webView.loadUrl(itemsList.get(itemPosition).getUrl());
				}
			}
			return true;
		}

		void doNotLoadUrl() {
			this.loadUrl = false;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER_VIEW);
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			WebBackForwardList forwardList = webView.copyBackForwardList();
			if (forwardList.getCurrentIndex() > 0) {
				String url = forwardList.getItemAtIndex(forwardList.getCurrentIndex() - 1).getUrl();
				int positionByURL = getPositionByURL(url);
				if (actionBar.getSelectedNavigationIndex() != positionByURL) {
					navigationListener.doNotLoadUrl();
					actionBar.setSelectedNavigationItem(positionByURL);
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
			(new DialogBuilder()).buildAndShowAboutDialog(this);
			break;
		default:
			break;
		}
		return true;
	}

	private ArrayList<String> getItemNameList(List<GridItem> items) {
		ArrayList<String> result = new ArrayList<String>();
		for (GridItem item : items) {
			result.add(item.getName());
		}
		return result;
	}

	public int getPositionByURL(String url) {
		int res = 0;
		if (url == null || url.isEmpty()) {
			return res;
		}
		for (int i = 0; i < itemsList.size(); i++) {
			GridItem item = itemsList.get(i);
			if (url.contains(item.getUrl())) {
				res = i;
			}
		}
		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.web_view, menu);
		refreshMenuItem = menu.findItem(R.id.progress);
		return super.onCreateOptionsMenu(menu);
	}
}
