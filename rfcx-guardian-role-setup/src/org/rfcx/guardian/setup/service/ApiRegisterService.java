package org.rfcx.guardian.setup.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.rfcx.guardian.setup.RfcxGuardian;
import org.rfcx.guardian.utility.http.HttpPostMultipart;
import org.rfcx.guardian.utility.rfcx.RfcxLog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ApiRegisterService extends Service {

	private static final String TAG = "Rfcx-"+RfcxGuardian.APP_ROLE+"-"+ApiRegisterService.class.getSimpleName();
	
	private static final String SERVICE_NAME = "ApiRegister";

	private RfcxGuardian app;
	
	private boolean runFlag = false;
	private ApiRegister apiRegister;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.apiRegister = new ApiRegister();
		app = (RfcxGuardian) getApplication();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.v(TAG, "Starting service: "+TAG);
		this.runFlag = true;
		app.rfcxServiceHandler.setRunState(SERVICE_NAME, true);
		try {
			this.apiRegister.start();
		} catch (IllegalThreadStateException e) {
			RfcxLog.logExc(TAG, e);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		app.rfcxServiceHandler.setRunState(SERVICE_NAME, false);
		this.apiRegister.interrupt();
		this.apiRegister = null;
	}
	
	private class ApiRegister extends Thread {

		public ApiRegister() {
			super("ApiRegisterService-ApiRegister");
		}

		@Override
		public void run() {
			ApiRegisterService apiRegisterService = ApiRegisterService.this;

			HttpPostMultipart httpPostMultipart = new HttpPostMultipart();
			// setting customized rfcx authentication headers (necessary for API access)
			List<String[]> rfcxAuthHeaders = new ArrayList<String[]>();
			rfcxAuthHeaders.add(new String[] { "x-auth-user", "register" });
			rfcxAuthHeaders.add(new String[] { "x-auth-token", app.rfcxPrefs.getPrefAsString("install_api_registration_token") });
			httpPostMultipart.setCustomHttpHeaders(rfcxAuthHeaders);

			try {
				if (app.deviceConnectivity.isConnected()) {
					if (app.apiCore.apiRegisterEndpoint != null) {
						String postUrl =	(((app.rfcxPrefs.getPrefAsString("api_url_base")!=null) ? app.rfcxPrefs.getPrefAsString("api_url_base") : "https://api.rfcx.org")
										+ app.apiCore.apiRegisterEndpoint
										);
						
						List<String[]> registrationParameters = new ArrayList<String[]>();
						registrationParameters.add(new String[] {  "guid", app.rfcxDeviceId.getDeviceGuid() });
						registrationParameters.add(new String[] {  "token", app.rfcxDeviceId.getDeviceToken() });
						
						String stringRegistrationResponse = httpPostMultipart.doMultipartPost(postUrl, registrationParameters, new ArrayList<String[]>());
						JSONArray jsonRegistrationResponse = new JSONArray(stringRegistrationResponse);
						
						Log.d(TAG, stringRegistrationResponse);
						
					} else {
						Log.d(TAG, "Cancelled because apiRegisterEndpoint is null...");
					}
				} else {
					Log.d(TAG, "Cancelled because there is no internet connectivity...");
				}
			} catch (Exception e) {
				RfcxLog.logExc(TAG, e);
			} finally {
				app.rfcxServiceHandler.setRunState(SERVICE_NAME, false);
				app.rfcxServiceHandler.stopService(SERVICE_NAME);
			}
		}
	}

}
