package fr.nawrasg.atlantis.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras GEORGI on 04/12/2015.
 */
public class GeoService extends IntentService {

	public GeoService() {
		super("GeoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean result = intent.getBooleanExtra("entering", false);
		if(result){
			boolean enter_wifi = App.getPrefBoolean(this, "geo_enter_wifi");
			boolean enter_alarm = App.getPrefBoolean(this, "geo_enter_alarm");
			if(enter_wifi){
				WifiManager nWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				nWifi.setWifiEnabled(true);
			}
			if(enter_alarm){

			}
		}else{
			boolean exit_wifi = App.getPrefBoolean(this, "geo_exit_wifi");
			boolean exit_alarm = App.getPrefBoolean(this, "geo_exit_alarm");
			if(exit_wifi){
				WifiManager nWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				nWifi.setWifiEnabled(false);
			}
			if(exit_alarm){

			}
		}
	}
}
