package fr.nawrasg.atlantis.receivers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Set;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.services.WifiService;

public class WifiReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager nCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nNetwork = nCM.getActiveNetworkInfo();
		if (nNetwork != null) {
			if (nNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				Set<String> nWifiSet = App.getPrefSet(context, "wifiSet");
				WifiManager nWM = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				String nCurrentWifi = nWM.getConnectionInfo().getBSSID();
				if (nWifiSet.contains(nCurrentWifi)) {
					ComponentName comp = new ComponentName(context.getPackageName(), WifiService.class.getName());
					startWakefulService(context, (intent.setComponent(comp)));
				}
			}
		}
	}

}
