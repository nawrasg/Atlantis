package fr.nawrasg.atlantis.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.type.Alarm;

/**
 * Created by Nawras GEORGI on 04/12/2015.
 */
public class GeoService extends IntentService {

	public GeoService() {
		super("GeoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		AudioManager nAM = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		WifiManager nWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		boolean result = intent.getBooleanExtra("entering", false);
		if(result){
			boolean enter_wifi = App.getPrefBoolean(this, "geo_enter_wifi");
			boolean enter_alarm = App.getPrefBoolean(this, "geo_enter_alarm");
			String enter_profile = App.getString(this, "geo_enter_profile");
			if(enter_wifi){
				nWifi.setWifiEnabled(true);
			}
			if(enter_alarm){
				(new Alarm(this)).setMode(Alarm.MODE_DAY);
			}
			switch(enter_profile){
				case "ring":
					nAM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					break;
				case "viber":
					nAM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					break;
				case "silence":
					nAM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					break;
			}
		}else{
			boolean exit_wifi = App.getPrefBoolean(this, "geo_exit_wifi");
			boolean exit_alarm = App.getPrefBoolean(this, "geo_exit_alarm");
			String exit_profile = App.getString(this, "geo_exit_profile");
			if(exit_wifi){
				nWifi.setWifiEnabled(false);
			}
			if(exit_alarm){
				(new Alarm(this)).setMode(Alarm.MODE_AWAY);
			}
			switch(exit_profile){
				case "ring":
					nAM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					break;
				case "viber":
					nAM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					break;
				case "silence":
					nAM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					break;
			}
		}
	}
}
