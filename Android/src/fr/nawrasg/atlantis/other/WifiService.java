package fr.nawrasg.atlantis.other;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.async.DataGET;

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
				Toast.makeText(nContext, "Connecté à Atlantis !", Toast.LENGTH_SHORT).show();
				new DataGET(nContext, false).execute(App.WELCOME);
			}

		});

		WifiReceiver.completeWakefulIntent(intent);
	}

}
