package fr.nawrasg.atlantis.other;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataPUT;

public class GCMService extends IntentService {
	public static Ringtone RINGTONE;
	public static final int NOTIFICATION_ID = 1;
	public static final int NOTIFICATIONLED_ID = 2;
	private NotificationManager mNotificationManager;
	private Vibrator nVibrate;
	private LocationManager nLM;
	private Context nContext;
	NotificationCompat.Builder builder;

	public GCMService() {
		super("GCMService");
	}

	private void sendPosition() {
		Handler nHandler = new Handler(getMainLooper());
		nHandler.post(new Runnable() {

			@Override
			public void run() {
				nLM.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						new DataPUT(nContext, false).execute(App.GEO, "lat=" + location.getLatitude() + "&long=" + location.getLongitude());
					}

					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
					}

					@Override
					public void onProviderEnabled(String provider) {
					}

					@Override
					public void onProviderDisabled(String provider) {
					}

				}, null);
			}

		});
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		nLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		nContext = this;

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String nTitle = extras.getString("title");
				String nMessage = extras.getString("message");
				switch (nTitle) {
					case "at_commands":
						parseCommand(nMessage);
						break;
					default:
						if(nTitle != null && nMessage != null)
							sendNotification(nTitle, nMessage);
				}

			}
		}
		GCMReceiver.completeWakefulIntent(intent);
	}

	private void parseCommand(String command) {
		switch (command) {
			case "geo":
				sendPosition();
				sendNotification("Atlantis", "Position envoyée !");
				break;
			case "geoi":
				sendPosition();
				break;
			case "ring":
				ring();
				break;
		}
	}

	private void ring() {
		AudioManager nAM = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		nAM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		int maxVolume = nAM.getStreamMaxVolume(AudioManager.STREAM_RING);
		nAM.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES);
		RINGTONE = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
		RINGTONE.play();

		NotificationManager nNM = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent nIntent = new Intent(this, RingingReceiver.class);
		nIntent.putExtra("id", NOTIFICATION_ID);
		PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, nIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.home)
				.setContentTitle("Atlantis")
				.setContentText("Pour arrêter la sonnerie, cliquez sur Trouvé !")
				.addAction(R.drawable.ic_notifications_off_black_24dp, "Trouvé !", contentIntent);
		nNM.notify(NOTIFICATION_ID, mBuilder.build());
		try {
			Thread.sleep(1000 * 30);
			RINGTONE.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void sendNotification(String title, String msg) {
		AudioManager nAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainFragmentActivity.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.home)
				.setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		if (nAudio.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
			nVibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			long[] nPattern = { 0, 500, 500, 500, 500, 500 };
			nVibrate.vibrate(nPattern, -1);
		}
		Notification nNotify = new Notification();
		nNotify.ledARGB = Color.BLUE;
		nNotify.flags |= Notification.FLAG_SHOW_LIGHTS;
		nNotify.ledOnMS = 1;
		nNotify.ledOffMS = 0;
		mNotificationManager.notify(NOTIFICATIONLED_ID, nNotify);
	}

}
