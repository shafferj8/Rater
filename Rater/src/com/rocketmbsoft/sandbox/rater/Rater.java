package com.rocketmbsoft.sandbox.rater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;


public class Rater {
	private static final String TAG = Rater.class.getSimpleName();
	private static final boolean D = false;
	private static final String PREF_I_LAUNCH_COUNT = "pref_launch_count";
	private static final String PREF_B_NEVER_REMIND = "pref_never_remind";
	String appName = null;
	String appId = null;
	int interval = 5;

	int prefLaunchCount = 0;
	boolean prefNeverRemind = false;
	Context context = null;
	SharedPreferences prefs = null;

	AlertDialog.Builder builder = null;

	public Rater(Context context, SharedPreferences p, String appName, String appId) {

		prefs = p;

		load();

		this.appName = appName;
		this.appId = appId;

		this.context = context;

		if ( ! prefNeverRemind) {
			builder = new AlertDialog.Builder(context);
			builder.setMessage(
					"Thank you for using "+appName+", it would mean "+
					"a lot to us if you took a minute to give us 5 stars "+
			"in the android market.").
			setCancelable(false).setNeutralButton("Not Now", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					save();

					dialog.cancel();
					// Open URL
				}
			}).
			setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					prefNeverRemind = true;
					save();

					try {
						Intent goToMarket = null;
						
						goToMarket = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("market://details?id="+Rater.this.appId));
						
						Rater.this.context.startActivity(goToMarket);
					} catch (Exception e) {

					}

				}
			}).setNegativeButton("Don't Remind Me Again", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					prefNeverRemind = true;
					save();
					dialog.cancel();
				}
			});
		}
	}

	public void load() {

		prefLaunchCount = prefs.getInt(PREF_I_LAUNCH_COUNT, 0);
		prefNeverRemind = prefs.getBoolean(PREF_B_NEVER_REMIND, false);

		prefLaunchCount++;
	}

	public void save() {
		Editor e = prefs.edit();

		e.putInt(PREF_I_LAUNCH_COUNT, prefLaunchCount);
		e.putBoolean(PREF_B_NEVER_REMIND, prefNeverRemind);

		e.commit();
	}

	public void run() {

		if (D) Log.d(TAG, "Launch Count : "+prefLaunchCount+", Never Remind : "+prefNeverRemind+
				" mod value : "+(prefLaunchCount % interval) );

		if (prefNeverRemind) {
			return;
		}

		if ((prefLaunchCount % interval) != 0) {

			save();
			return;
		}

		if (D) Log.d(TAG, "Creating Dialog box");

		prefLaunchCount = 0;
		builder.create().show();

		save();
	}

};

