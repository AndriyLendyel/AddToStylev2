package com.rubyapps.addictedtostyle.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.actionbarsherlock.widget.ShareActionProvider.OnShareTargetSelectedListener;
import com.appodeal.ads.Appodeal;
import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.adapter.MyGridViewAdapter;
import com.rubyapps.addictedtostyle.app.AppConfig;
import com.rubyapps.addictedtostyle.app.MyApplication;
import com.rubyapps.addictedtostyle.helper.ParseUtils;
import com.rubyapps.addictedtostyle.model.GridItem;

public class MainActivity extends SherlockActivity {

    private ShareActionProvider mShareActionProvider;
    private GridView gridView;

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
	Appodeal.disableLocationPermissionCheck();
	Appodeal.initialize(this, AppConfig.AD_APP_KEY, Appodeal.BANNER);
	Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    @Override
    public void onResume() {
	super.onResume();
	Appodeal.onResume(this, Appodeal.BANNER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getSupportMenuInflater().inflate(R.menu.main, menu);
	/**
	 * Getting the actionprovider associated with the menu item whose id is
	 * share
	 */
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
	String shareBody = "Please provide this text. https://play.google.com/store/apps/details?id=";
	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject to provide");
	intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + getPackageName());
	intent.setType("text/plain");
	return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menu_about:
	    buildAndShowDialog();
	    break;
	case R.id.menu_settings:
	    Intent intent = new Intent(this, SettingsActivity.class);
	    startActivity(intent);
	    break;
	default:
	    break;
	}
	return true;
    }

    private void buildAndShowDialog() {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	// set title
	alertDialogBuilder.setTitle(Html.fromHtml("<b>" + "Addicted to Style" + "</b>"));
	alertDialogBuilder.setIcon(R.drawable.ic_launcher);
	// set dialog message
	View layout = getLayoutInflater().inflate(R.layout.dialog, null);
	try {
	    ((TextView) layout.findViewById(R.id.textVersion)).setText(Html.fromHtml("<b>" + "Version "
		    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "</b> <br>Developed by " + "<b>" + "Ruby Apps" + "</b>"));
	    ((TextView) layout.findViewById(R.id.textCopyright)).setText(Html.fromHtml("<b>"
		    + "Copyright \u00A9 2015 </b> <br> Build version 4.2.0/319"));
	    alertDialogBuilder.setView(layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
		    dialog.cancel();
		}
	    });
	} catch (NameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	AlertDialog alertDialog = alertDialogBuilder.create();
	alertDialog.show();
    }

}