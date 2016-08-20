package fr.nawrasg.atlantis.fragments.preferences;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.SettingsActivity;
import fr.nawrasg.atlantis.other.AtlantisContract;

public class DiversPreferenceFragment extends PreferenceFragment implements OnPreferenceClickListener {
	private Context mContext;
	private Preference mImportPreference, mGcmPreference;
	private EditTextPreference mSyncFrequencyPreference;
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

		mSyncFrequencyPreference = (EditTextPreference) findPreference("sync_frequency");
		mSyncFrequencyPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Account nAccount = new Account(getString(R.string.app_name), AtlantisContract.AUTHORITY);
				long nPeriod = Integer.parseInt(App.getString(mContext, "sync_frequency")) * 3600L;
				if(nPeriod > 0){
					ContentResolver.addPeriodicSync(nAccount, AtlantisContract.AUTHORITY, Bundle.EMPTY, nPeriod);
					((SettingsActivity)getActivity()).launchSync();
					return true;
				}
				return false;
			}
		});

		mImportPreference = findPreference("import");
		mImportPreference.setOnPreferenceClickListener(this);
		
		mGcmPreference = findPreference("gcm");
		mGcmPreference.setOnPreferenceClickListener(this);
		if(!App.getPrefString(mContext, PROPERTY_REG_ID).equals("")){
			mGcmPreference.setSummary(getResources().getString(R.string.fragment_preference_divers_gcm_registered_device));
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch (preference.getKey()) {
			case "import":
				importSettings();
				return true;
			case "gcm":
				GcmSubscription();
				return true;
		}
		return false;
	}

	private void importSettings(){
		String nURL = App.getFullUrl(mContext) + App.SETTINGS + "?api=" + App.getAPI(mContext) + "&type=Atlantis";
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				try {
					JSONObject nJson = new JSONObject(response.body().string());
					if (nJson.getString("name").equals("Atlantis")) {
						importSettings(nJson);
					} else {
						Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_settings_import_fail), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
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
			Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_gcm_registration_done), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_gcm_not_available), Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getRegistrationId() {
		String registrationId = App.getString(mContext, PROPERTY_REG_ID);
		if (registrationId.isEmpty()) {
			Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_gcm_registration_progress), Toast.LENGTH_SHORT).show();
			return "";
		}
		Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_gcm_registered_device), Toast.LENGTH_SHORT).show();
		return registrationId;
	}
	
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_gcm_not_available), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_divers_settings_import_success), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
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
					regGCM(nRegId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

		}.execute(null, null, null);
	}

	private void regGCM(String data){
		String nURL = App.getFullUrl(mContext) + App.GCM + "?api=" + App.getAPI(mContext) + "&gcm=" + data;
		Request nRequest = new Request.Builder().url(nURL).post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), "")).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {

			}
		});
	}

}
