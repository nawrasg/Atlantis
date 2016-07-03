package fr.nawrasg.atlantis.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.nawrasg.atlantis.services.GCMService;

public class RingingReceiver extends BroadcastReceiver {
	public RingingReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (GCMService.RINGTONE != null) {
			GCMService.RINGTONE.stop();
		}
		int nID = intent.getIntExtra("id", 0);
		NotificationManager nNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nNM.cancel(nID);
	}
}
