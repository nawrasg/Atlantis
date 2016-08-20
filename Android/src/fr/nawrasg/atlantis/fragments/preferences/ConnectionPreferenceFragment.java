package fr.nawrasg.atlantis.fragments.preferences;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.SettingsActivity;
import fr.nawrasg.atlantis.other.CheckConnection;

public class ConnectionPreferenceFragment extends PreferenceFragment implements OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private Context mContext;
    private Preference mWifiPreference;
    private EditTextPreference mURLPreference, mIPPreference;
    private WifiManager mWM;
    private CheckConnection mConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_connection);
        mContext = getActivity();
        mWM = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        mConnection = new CheckConnection(mContext);
        mURLPreference = (EditTextPreference) findPreference("urlExterne");
        mURLPreference.setOnPreferenceChangeListener(this);
        mIPPreference = (EditTextPreference) findPreference("urlInterne");
        mIPPreference.setOnPreferenceChangeListener(this);
        mWifiPreference = findPreference("wifiHome");
        mWifiPreference.setOnPreferenceClickListener(this);
        mWifiPreference.setTitle(App.getPrefSetSize(mContext, "wifiSet") + " " + getResources().getString(R.string.fragment_preference_connection_wifi_description_registered));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "wifiHome":
                if (mConnection.checkConnection() == CheckConnection.TYPE_WIFI) {
                    String y = mWM.getConnectionInfo().getSSID();
                    App.addPrefSetString(mContext, "wifiSet", y);
                    mWifiPreference.setTitle(App.getPrefSetSize(mContext, "wifiSet") + " " + getResources().getString(R.string.fragment_preference_connection_wifi_description_registered));
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.fragment_preference_connection_wifi_offline), Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((SettingsActivity)getActivity()).launchSync();
        return true;
    }
}
