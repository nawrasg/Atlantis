package fr.nawrasg.atlantis;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationContextProvider extends Application{
	public static RequestQueue mVolleyRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mVolleyRequestQueue.start();
	}

	@Override
	public void onTerminate() {
		mVolleyRequestQueue.stop();
		super.onTerminate();
	}
}
