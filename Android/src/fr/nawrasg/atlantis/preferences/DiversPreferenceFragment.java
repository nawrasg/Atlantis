package fr.nawrasg.atlantis.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;

public class DiversPreferenceFragment extends PreferenceFragment implements OnPreferenceClickListener {
	private Context mContext;
	private Preference mImportPreference, mGcmPreference;
	private GoogleCloudMessaging mGCM;
	private String nRegId;
	private String SENDER_ID = "926480811038";
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String PROPERTY_REG_ID = "registration_id";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_divers);
		mContext = getActivity();

		mImportPreference = findPreference("import");
		mImportPreference.setOnPreferenceClickListener(this);
		
		mGcmPreference = findPreference("gcm");
		mGcmPreference.setOnPreferenceClickListener(this);
		if(!App.getPrefString(mContext, PROPERTY_REG_ID).equals("")){
			mGcmPreference.setSummary("Appareil enregistré");
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch (preference.getKey()) {
			case "import":
				new SettingsGET(mContext).execute(App.SETTINGS, "type=Atlantis");
				return true;
			case "gcm":
				GcmSubscription();
				return true;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == App.CODE_PLAN) {
			setPlanPath(data.getData());
		}
	}
	
	private void GcmSubscription(){
		if (checkPlayServices()) {
			mGCM = GoogleCloudMessaging.getInstance(mContext);
			nRegId = getRegistrationId();
			registerGCM();
			Toast.makeText(mContext, "Fin d'enregistrement !", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "Les services Google Play ne sont pas disponibles !", Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getRegistrationId() {
		String registrationId = App.getString(mContext, PROPERTY_REG_ID);
		if (registrationId.isEmpty()) {
			Toast.makeText(mContext, "Enregistrement en cours...", Toast.LENGTH_SHORT).show();
			return "";
		}
		Toast.makeText(mContext, "Déjà enregistré !", Toast.LENGTH_SHORT).show();
		return registrationId;
	}
	
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(mContext, "Les services Google Play ne sont pas disponibles !", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		return true;
	}

	public void setPlanPath(Uri uri) {
		App.setString(mContext, "plan", uri.toString());
	}

	private void importSettings(JSONObject json) {
		try {
			App.setString(mContext, "urlExterne", json.getString("url"));
			//App.setString(mContext, "wifiSSID", json.getString("ssid"));
			//App.setString(mContext, "wifiBSSID", json.getString("mac"));
			App.setFloat(mContext, "homeRadius", (float) json.getDouble("radius"));
			App.setFloat(mContext, "homeLong", (float) json.getDouble("long"));
			App.setFloat(mContext, "homeLat", (float) json.getDouble("lat"));
			Toast.makeText(mContext, "Paramètres importés avec succès !", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private class SettingsGET extends DataGET {

		public SettingsGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject nJson = new JSONObject(result);
				if (nJson.getString("name").equals("Atlantis")) {
					importSettings(nJson);
				} else {
					Toast.makeText(mContext, "Impossible de récupérer les paramètres !", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
		}

	}
	
	private void registerGCM() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					if (mGCM == null) {
						mGCM = GoogleCloudMessaging.getInstance(mContext);
					}
					nRegId = mGCM.register(SENDER_ID);
					App.setString(mContext, PROPERTY_REG_ID, nRegId);
					Looper.prepare();
					new DataPOST(mContext, false).execute(App.GCM, "gcm=" + nRegId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

		}.execute(null, null, null);
	}

}
