package com.rubyapps.addictedtostyle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;

public class WebViewActivity extends Activity {

	private WebView webView;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		Appodeal.show(this, Appodeal.BANNER_BOTTOM);
		webView = (WebView) findViewById(R.id.webView);
		// webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (Build.VERSION.SDK_INT >= 11) {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		CookieSyncManager.createInstance(this);
		// webView.setWebViewClient(new fv(this, (byte)0));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setLightTouchEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		if (Build.VERSION.SDK_INT > 7) {
			webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
		}
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			 @Override
			    public void onPageStarted(WebView view, String url, Bitmap favicon) {
			        super.onPageStarted(view, url, favicon);
					/*long downTime = SystemClock.uptimeMillis();
					long eventTime = SystemClock.uptimeMillis() + 100;
					float x = 0.0f;
					float y = 0.0f;
					// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
					int metaState = 0;
					MotionEvent motionEvent = MotionEvent.obtain(
					    downTime, 
					    eventTime, 
					    MotionEvent.ACTION_UP, 
					    x, 
					    y, 
					    metaState
					);

					// Dispatch touch event to view
					view.dispatchTouchEvent(motionEvent);*/
			}
			 
			 @Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				webView.postInvalidate();
			}
		});
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
		webView.clearCache(true);
		webView.loadUrl(getIntent().getStringExtra("url"));

	}

	@Override
	public void onResume() {
		super.onResume();
		Appodeal.onResume(this, Appodeal.BANNER);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (webView != null) {
			webView.stopLoading();
			webView.onPause(); // pauses background threads, stops playing sound
			webView.pauseTimers(); // pauses the WebViewCore
			CookieSyncManager.getInstance().stopSync();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.clearView();
		webView.clearCache(true);
		webView.clearHistory();
		//webView.destroy();
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

}
