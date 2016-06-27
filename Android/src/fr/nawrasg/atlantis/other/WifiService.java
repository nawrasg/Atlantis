package fr.nawrasg.atlantis.other;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;

public class WifiService extends IntentService {

	public WifiService() {
		super("WifiService");
	}

	public WifiService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Context nContext = this;
		Handler nHandler = new Handler(getMainLooper());
		nHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(nContext, nContext.getString(R.string.service_wifi_atlantis_connected), Toast.LENGTH_SHORT).show();
				String nURL = App.getFullUrl(nContext) + App.WELCOME + "?api=" + App.getAPI(nContext);
				Request nRequest = new Request.Builder().url(nURL).build();
				App.httpClient.newCall(nRequest).enqueue(new Callback() {
					@Override
					public void onFailure(Request request, IOException e) {

					}

					@Override
					public void onResponse(Response response) throws IOException {

					}
				});
			}

		});

		WifiReceiver.completeWakefulIntent(intent);
	}

}
