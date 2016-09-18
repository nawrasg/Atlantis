package fr.nawrasg.atlantis.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;

import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.services.GeoService;

public class GeoReceiver extends WakefulBroadcastReceiver {
    private NotificationManager nNM;
    private Notification nN;
    private Vibrator nVibrate;

    @Override
    public void onReceive(Context context, Intent intent) {
        nNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        final String key = LocationManager.KEY_PROXIMITY_ENTERING;
        final Boolean entering = intent.getBooleanExtra(key, false);
        String result = "";
        if (entering) {
            result = context.getString(R.string.geo_enter_message);
        } else {
            result = context.getString(R.string.geo_exit_message);
        }
        Intent nIntent = new Intent(context, GeoService.class);
        nIntent.putExtra("entering", entering);
        context.startService(nIntent);
        nN = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(result)
                .setSmallIcon(R.mipmap.ng_home_notifications)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
        nNM.notify(0, nN);
        nVibrate.vibrate(500);

    }

}
