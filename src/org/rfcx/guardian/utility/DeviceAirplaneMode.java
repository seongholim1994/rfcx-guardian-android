package org.rfcx.guardian.utility;

import org.rfcx.guardian.RfcxGuardian;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class DeviceAirplaneMode {

	private static final String TAG = "RfcxGuardian-"+DeviceAirplaneMode.class.getSimpleName();
	private static final String NULL_EXC = "Exception thrown, but exception itself is null.";
	
	private boolean isEnabled;
	
	private RfcxGuardian app = null;
	
	public boolean isEnabled(Context context) {
		isEnabled = Settings.System.getInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		return isEnabled;
	}
	
	public void setOn(Context context) {
		if (app == null) { app = (RfcxGuardian) context.getApplicationContext(); }
		if (app.verboseLog) { Log.d(TAG, "Turning AirplaneMode ON"); }
    	if (!isEnabled(context)) {
    		set(context, 1);
    	}
	}
	
	public void setOff(Context context) {
		if (app == null) { app = (RfcxGuardian) context.getApplicationContext(); }
		if (app.verboseLog) { Log.d(TAG, "Turning AirplaneMode OFF"); }
    	if (!isEnabled(context)) {
    		set(context, 1);
    		set(context, 0);
    	} else {
    		set(context, 0);
    	}
    	Log.d(TAG, "Allow WiFi: "+app.sharedPrefs.getBoolean("allow_wifi", false)+"");
    	Log.d(TAG, "Allow Bluetooth: "+app.sharedPrefs.getBoolean("allow_bluetooth", false)+"");
	}
	
	private void set(Context context, int value) {
		try {
			Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, value);
        	Intent intentAp = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        	intentAp.putExtra("state", (value == 1) ? true : false);
        	context.sendBroadcast(intentAp);
		} catch (Exception e) {
			Log.e(TAG,(e!=null) ? e.getMessage() : NULL_EXC);
		}
	}
	
	public boolean allowWifi(Context context) {
		if (app == null) { app = (RfcxGuardian) context.getApplicationContext(); }
		return app.sharedPrefs.getBoolean("allow_wifi", false);
	}
	
	public boolean allowBluetooth(Context context) {
		if (app == null) { app = (RfcxGuardian) context.getApplicationContext(); }
		return app.sharedPrefs.getBoolean("allow_bluetooth", false);
	}
	
}