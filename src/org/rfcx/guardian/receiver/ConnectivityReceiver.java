package org.rfcx.guardian.receiver;

import org.rfcx.guardian.RfcxGuardian;
import org.rfcx.guardian.utility.TimeOfDay;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

	private static final String TAG = ConnectivityReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
        RfcxGuardian app = (RfcxGuardian) context.getApplicationContext();
        final boolean isConnected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        
//        TimeOfDay timeOfDay = new TimeOfDay();
//        app.apiCore.networkConnectivity = isConnected;
		
		if (app.verboseLog) Log.d(TAG, "Connectivity Detected... (RfcxSource)");
		if (isConnected) {
////			app.apiComm.sendAnyAlerts(context);
//			if (timeOfDay.isDataGenerationEnabled(context) || app.ignoreOffHours) {
//				if (app.verboseLogging) Log.d(TAG, "Check-in request allowed. Doing it.");
//				//app.apiCheckIn.sendCheckIn(context);
//			} else {
//				if (app.verboseLogging) Log.d(TAG, "Check-in not allowed right now.");
//			}
		}
	}

}
