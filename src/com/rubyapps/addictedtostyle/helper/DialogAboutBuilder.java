package com.rubyapps.addictedtostyle.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.rubyapps.addictedtostyle.R;

public class DialogAboutBuilder {

    public void buildAndShowDialog(Activity context) {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	alertDialogBuilder.setTitle(Html.fromHtml("<b>" + "Addicted to Style" + "</b>"));
	alertDialogBuilder.setIcon(R.drawable.ic_launcher);
	View layout = context.getLayoutInflater().inflate(R.layout.dialog, null);
	try {
	    ((TextView) layout.findViewById(R.id.textVersion)).setText(Html.fromHtml("<b>" + "Version "
		    + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "</b> <br>Developed by " + "<b>" + "Ruby Apps" + "</b>"));
	    ((TextView) layout.findViewById(R.id.textCopyright)).setText(Html.fromHtml("<b>"
		    + "Copyright \u00A9 2015 </b> <br> Build version 4.4.0/23"));
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

}
