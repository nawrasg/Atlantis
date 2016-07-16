package fr.nawrasg.atlantis.services;

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
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.receivers.GCMReceiver;
import fr.nawrasg.atlantis.receivers.RingingReceiver;

public class GCMService extends IntentService {
	public static Ringtone RINGTONE;
	public static int NOTIFICATION_ID = 1;
	public static final int NOTIFICATIONLED_ID = 2;
	private NotificationManager mNotificationManager;
	private Vibrator nVibrate;
	private LocationManager nLM;
	private Context mContext;
	private OkHttpClient mClient;

	public GCMService() {
		super("GCMService");
		mClient = new OkHttpClient();
	}

	private void sendPosition() {
		Handler nHandler = new Handler(getMainLooper());
		nHandler.post(new Runnable() {

			@Override
			public void run() {
				nLM.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						String nURL = App.getFullUrl(mContext) + App.GEO + "?api=" + App.getAPI(mContext) + "&lat=" + location.getLatitude() + "&long=" + location.getLongitude() + "&speed=" + location.getSpeed() + "&bearing=" + location.getBearing();
						Request nRequest = new Request.Builder()
								.url(nURL)
								.put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
								.build();
						mClient.newCall(nRequest).enqueue(new Callback() {
							@Override
							public void onFailure(Request request, IOException e) {

							}

							@Override
							public void onResponse(Response response) throws IOException {

							}
						});
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
		mContext = this;

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
				sendNotification(mContext.getString(R.string.app_name), mContext.getString(R.string.service_gcm_position_send));
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

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.home)
				.setContentTitle(mContext.getString(R.string.app_name))
				.setContentText(mContext.getString(R.string.service_gcm_ring_description))
				.addAction(R.drawable.ic_notifications_off_black_24dp, mContext.getString(R.string.service_gcm_ring_found), contentIntent);
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

		int nIcon = R.drawable.home;
		long nTime = System.currentTimeMillis();
		Intent nIntent = new Intent(this, MainActivity.class);
		nIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent nPI = PendingIntent.getActivity(this, 0, nIntent, 0);
		Notification nNotification = new Notification.Builder(this)
				.setContentTitle(title)
				.setContentText(msg)
				.setSmallIcon(nIcon)
				.setContentIntent(nPI)
				.build();
		nNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOTIFICATION_ID, nNotification);

		NOTIFICATION_ID++;

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
