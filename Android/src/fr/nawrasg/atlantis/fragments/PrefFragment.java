package fr.nawrasg.atlantis.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.other.CheckConnection;

public class PrefFragment extends PreferenceFragment {
	private CheckBoxPreference nSync;
	private Preference nWifi, nGCM, mPlan, mAdmin;
	private Preference nExport;
	private WifiManager nWM;
	private CheckConnection nIC;
	private Context mContext;
	private GoogleCloudMessaging mGCM;
	private String SENDER_ID = "926480811038";
	private String nRegId;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	// public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		nWM = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		mContext = getActivity();
		getActivity().getActionBar().setIcon(R.drawable.ng_settings);
		nIC = new CheckConnection(mContext);
		nWifi = findPreference("wifiHome");
		nWifi.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (nIC.checkConnection() == CheckConnection.TYPE_WIFI) {
					String x = nWM.getConnectionInfo().getSSID();
					String y = nWM.getConnectionInfo().getBSSID();
					App.setString(mContext, "wifiSSID", x);
					App.setString(mContext, "wifiBSSID", y);
					nWifi.setTitle(x + "(" + y + ")");
					return true;
				} else {
					Toast.makeText(getActivity(), "Wifi n'est pas activé !", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		});
		nExport = findPreference("import");
		nExport.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				getSettings();
				return false;
			}
		});
		nGCM = findPreference("gcm");
		nGCM.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (checkPlayServices()) {
					mGCM = GoogleCloudMessaging.getInstance(mContext);
					nRegId = getRegistrationId();
					// if (nRegId.isEmpty()) {
					registerGCM();
					// }
					Toast.makeText(mContext, "Fin d'enregistrement !", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "Les services Google Play ne sont pas disponibles !", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		String nSSID = App.getString(mContext, "wifiSSID");
		String nBSSID = App.getString(mContext, "wifiBSSID");
		if (!nBSSID.equals("")) {
			nWifi.setTitle(nSSID + "(" + nBSSID + ")");
		}
		mPlan = findPreference("plan");
		mPlan.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, App.CODE_PLAN);
				return true;
			}
		});
		mAdmin = findPreference("admin");
		mAdmin.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {

				return true;
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

	public void setPlanPath(Uri uri) {
		App.setString(mContext, "plan", uri.toString());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

	}

	private void importSettings(JSONObject json) {
		try {
			App.setString(mContext, "urlExterne", json.getString("url"));
			App.setString(mContext, "wifiSSID", json.getString("ssid"));
			App.setString(mContext, "wifiBSSID", json.getString("mac"));
			App.setFloat(mContext, "homeRadius", (float) json.getDouble("radius"));
			App.setFloat(mContext, "homeLong", (float) json.getDouble("long"));
			App.setFloat(mContext, "homeLat", (float) json.getDouble("lat"));
			Toast.makeText(mContext, "Paramètres importés avec succès !", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
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

	private String getRegistrationId() {
		String registrationId = App.getString(mContext, PROPERTY_REG_ID);
		if (registrationId.isEmpty()) {
			Toast.makeText(mContext, "Enregistrement en cours...", Toast.LENGTH_SHORT).show();
			return "";
		}
		Toast.makeText(mContext, "Déjà enregistré !", Toast.LENGTH_SHORT).show();
		return registrationId;
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
					regGCM(nRegId);
				} catch (Exception e) {
					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				return null;
			}

		}.execute(null, null, null);
	}

	private void regGCM(String data){
		String nURL = App.getFullUrl(mContext) + App.GCM + "?api=" + App.getAPI(mContext) + "&" + data;
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

	private void getSettings(){
		String nURL = App.getFullUrl(mContext) + App.HOME + "?api=" + App.getAPI(mContext) + "&setup";
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
						Toast.makeText(mContext, "Impossible de récupérer les paramètres !", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}
}
