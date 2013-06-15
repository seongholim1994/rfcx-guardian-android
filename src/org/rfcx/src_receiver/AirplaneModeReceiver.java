package org.rfcx.src_receiver;

import java.util.Calendar;

import org.rfcx.src_android.RfcxSource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class AirplaneModeReceiver extends BroadcastReceiver {

	private static final String TAG = AirplaneModeReceiver.class.getSimpleName();
	
	private RfcxSource rfcxSource = null;
	private WifiManager wifiManager = null;
//	private LocationManager locationManager = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (rfcxSource == null) rfcxSource = (RfcxSource) context.getApplicationContext();
		if (wifiManager == null) wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		if (locationManager == null) locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		if (rfcxSource.verboseLogging) Log.d(TAG, "BroadcastReceiver: "+TAG+" - Enabled");
		
		if (!rfcxSource.airplaneMode.isEnabled(context)) {
			rfcxSource.apiComm.setSignalSearchStart(Calendar.getInstance());
			wifiManager.setWifiEnabled(rfcxSource.airplaneMode.getAllowWifi());
		}
	}
}
