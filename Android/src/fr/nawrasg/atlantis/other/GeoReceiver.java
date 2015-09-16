package fr.nawrasg.atlantis.other;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import fr.nawrasg.atlantis.R;

public class GeoReceiver extends BroadcastReceiver{
	private NotificationManager nNM;
	private Notification nN;
	private Vibrator nVibrate;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		nNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		nVibrate = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		final String key = LocationManager.KEY_PROXIMITY_ENTERING;
        final Boolean entering = intent.getBooleanExtra(key, false);
        String result = "";
        if (entering) {
            result = "Entrée";
        } else {
        	result = "Sortie";
        }
		nN = new Notification.Builder(context)
		.setContentTitle("Atlantis").setContentText(result).setSmallIcon(R.drawable.home)
		.setWhen(System.currentTimeMillis()).setAutoCancel(true).build();
		nNM.notify(0, nN);
		nVibrate.vibrate(500);
	}

}
