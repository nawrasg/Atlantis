package fr.nawrasg.atlantis;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.nawrasg.atlantis.other.CheckConnection;

public class App {
	public static final int CODE_PLAN = 5;
	public static final int VOICE_RECOGNITION_REQUEST = 0x10101;
	public static final String CALL_NOTIFIER = "backend/at_call_notifier.php";
	public static final String CAMERAS = "backend/at_cameras.php";
	public static final String COURSES = "backend/at_courses.php";
	public static final String CUISINE = "backend/at_cuisine.php";
	public static final String DEVICES = "backend/at_ccdevices.php";
	public static final String EAN = "backend/at_ean.php";
	public static final String ENTRETIEN = "backend/at_entretien.php";
	public static final String GCM = "backend/at_gcm.php";
	public static final String GEO = "backend/at_geo.php";
	public static final String HISTORY = "backend/at_history.php";
	public static final String HOME = "backend/at_home.php";
	public static final String Images = "backend/at_images.php";
	public static final String LIGHTS = "backend/at_lights.php";
	public static final String MUSIC = "backend/at_music.php";
	public static final String NOTIFY = "backend/at_notify.php";
	public static final String PHARMACIE = "backend/at_pharmacie.php";
	public static final String PLANTE = "backend/at_plants.php";
	public static final String SCENARIOS = "backend/at_scenario.php";
	public static final String SENSORS = "backend/at_sensors.php";
	public static final String SETTINGS = "backend/at_settings.php";
	public static final String SPEECH = "backend/at_speech.php";
	public static final String SYNC = "backend/at_sync.php";
	public static final String WELCOME = "backend/at_welcome.php";

	public static String getURL(Context c) {
		WifiManager nWM = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String nURL = "";
		Set<String> nWifiSet = getPrefSet(c, "wifiSet");
		if (prefs.getBoolean("wifi", false)) {
			if (new CheckConnection(c).checkConnection() == CheckConnection.TYPE_WIFI) {
				String nBSSID = nWM.getConnectionInfo().getBSSID();
				if (nWifiSet.contains(nBSSID)) {
					nURL = prefs.getString("urlInterne", "");
				} else {
					nURL = prefs.getString("urlExterne", "");
				}
			} else {
				nURL = prefs.getString("urlExterne", "");
			}
		} else {
			nURL = prefs.getString("urlExterne", "");
			if (nURL.equals("")) {
				nURL = prefs.getString("urlInterne", "");
			}
		}
		return nURL;
	}

	public static String getAPI(Context c){
		SharedPreferences nPrefs = PreferenceManager.getDefaultSharedPreferences(c);
		String nAPI = nPrefs.getString("api", "");
		if(!nAPI.equals("")){
			return nAPI;
		}
		WifiManager nWM = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		WifiInfo nWI = nWM.getConnectionInfo();
		return nWI.getMacAddress();
	}

	public static String getFullUrl(Context context){
		String nLink = "http";
		if(isSSL(context)){
			nLink += "s";
		}
		nLink += "://" + getURL(context) + "/";
		return nLink;
	}

	public static Set<String> getPrefSet(Context context, String key) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(context);
		return nPref.getStringSet(key, new HashSet<String>());
	}
	
	public static void addPrefSetString(Context context, String set, String value){
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(context);
		Set<String> nSet = getPrefSet(context, set);
		Set<String> nNewSet = new HashSet<>();
		Iterator<String> nIterator = nSet.iterator();
		while(nIterator.hasNext()){
			nNewSet.add(nIterator.next());
		}
		nNewSet.add(value);
		Editor nEditor = nPref.edit();
		nEditor.putStringSet(set, nNewSet);
		nEditor.commit();
	}
	
	public static int getPrefSetSize(Context context, String set){
		Set<String> nSet = getPrefSet(context, set);
		return nSet.size();
	}

	public static String getPrefString(Context c, String x) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getString(x, "");
	}

	public static int getPrefsInt(Context c, String x) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getInt(x, -999);
	}

	public static boolean getBoolean(Context c, String x) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getBoolean(x, false);
	}

	public static boolean isSSL(Context context) {
		return getBoolean(context, "ssl") && !isHome(context);
	}

	public static boolean isHome(Context c) {
		WifiManager nWM = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		CheckConnection nIC = new CheckConnection(c);
		if (prefs.getBoolean("wifi", false)) {
			if (nIC.checkConnection() == CheckConnection.TYPE_WIFI) {
				String nBSSID = nWM.getConnectionInfo().getBSSID();
				if (getPrefString(c, "wifiBSSID").equals(nBSSID)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getInt(Context c, String x) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getInt(x, -1);
	}

	public static void setInt(Context c, String x, int y) {
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		Editor nEdit = nPref.edit();
		nEdit.putInt(x, y);
		nEdit.commit();
	}

	public static void setFloat(Context c, String x, float y){
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		Editor nEdit = nPref.edit();
		nEdit.putFloat(x, y);
		nEdit.commit();
	}

	public static float getFloat(Context c, String x){
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getFloat(x, 0);
	}

	public static String getString(Context c, String x){
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		return nPref.getString(x, "");
	}

	public static void setString(Context c, String x, String y){
		SharedPreferences nPref = PreferenceManager.getDefaultSharedPreferences(c);
		Editor nEdit = nPref.edit();
		nEdit.putString(x, y);
		nEdit.commit();
	}

	public static  boolean isPIN(Context c){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		return prefs.getBoolean("pin", false);
	}

	public static double[] fStringtoDouble(String[] x){
		double[] dArray = new double[x.length];
		for(int i = 0; i < x.length; i++){
			dArray[i] = Double.parseDouble(x[i]);
		}
		return dArray;
	}


	public static float PixelToDp(Context context, float px){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static boolean isInt(String x){
		try{
			int nTemp = Integer.parseInt(x);
		}catch(Exception e){
			return false;
		}
		return true;
	}

}
