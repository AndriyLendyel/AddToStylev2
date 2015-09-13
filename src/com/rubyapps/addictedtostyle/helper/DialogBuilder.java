package com.rubyapps.addictedtostyle.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.activity.MainActivity;
import com.rubyapps.addictedtostyle.activity.WebViewActivity;
import com.rubyapps.addictedtostyle.app.AppConfig;

public class DialogBuilder {

	public void buildAndShowAboutDialog(Activity context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(Html.fromHtml("<b>" + "Addicted to Style" + "</b>"));
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		View layout = context.getLayoutInflater().inflate(R.layout.dialog, null);
		try {
			((TextView) layout.findViewById(R.id.textVersion)).setText(Html.fromHtml("<b>" + "Version "
					+ context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
					+ "</b> <br>Developed by " + "<b>" + "Ruby Apps" + "</b>"));
			((TextView) layout.findViewById(R.id.textCopyright)).setText(Html.fromHtml("<b>"
					+ "Copyright \u00A9 2015 </b> <br> Build version 4.4.0/25"));
			alertDialogBuilder.setView(layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void buildAndShowRateUsDialog(final Activity context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(Html.fromHtml("<b>" + "Rate this app" + "</b>"));
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder
				.setMessage("Do you enjoy using this App? Please could you take some seconds to rate it because that would be nice of you");
		alertDialogBuilder.setNegativeButton("Sure", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				SharedPreferences prefs = context.getSharedPreferences(AppConfig.MY_PREFS_NAME, context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean(AppConfig.SURE_PRESSED, true);
				editor.commit();
				final String appPackageName = context.getPackageName();
				try {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
		alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intentWeb = new Intent(context, WebViewActivity.class);
				intentWeb.putExtra("url", "http://addictedtostyle.net/what-do-you-dislike-about-the-app/");
				intentWeb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intentWeb);
			}
		});
		alertDialogBuilder.setPositiveButton("Later", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alertDialogBuilder.setCancelable(false);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
