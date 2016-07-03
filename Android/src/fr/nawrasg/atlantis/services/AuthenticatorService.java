package fr.nawrasg.atlantis.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import fr.nawrasg.atlantis.other.Authenticator;

/**
 * Created by Nawras GEORGI on 25/03/2016.
 */
public class AuthenticatorService extends Service {

	private Authenticator mAuthenticator;

	@Override
	public void onCreate() {
		super.onCreate();
		mAuthenticator = new Authenticator(this);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mAuthenticator.getIBinder();
	}
}
