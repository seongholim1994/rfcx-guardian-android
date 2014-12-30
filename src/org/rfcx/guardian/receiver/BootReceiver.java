package org.rfcx.guardian.receiver;

import org.rfcx.guardian.RfcxGuardian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	private static final String TAG = BootReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "BootReceiver Launching Intent Services");
		((RfcxGuardian) context.getApplicationContext()).onBootServiceTrigger();
	}

}
