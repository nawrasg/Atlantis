package fr.nawrasg.atlantis.fragments.preferences;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;

public class SecurityPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private Context mContext;
    private SwitchPreference mLocationPermission;
    private PreferenceCategory mMarshmallowCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_security);
        mContext = getActivity();
        mMarshmallowCategory = (PreferenceCategory) findPreference("MarshmallowPermissions");
        mLocationPermission = (SwitchPreference) findPreference("PermissionLocation");
        mLocationPermission.setOnPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mMarshmallowCategory.setEnabled(false);
        } else {
            checkPermissions();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "PermissionLocation":
                if ((boolean) newValue) {
                    requestLocationPermission();
                    mLocationPermission.setChecked(true);
                }
                return true;
        }
        return false;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, App.PERMISSION_LOCATION);
    }

    public void setPermissionResult(int type, boolean result) {
        switch (type) {
            case App.PERMISSION_LOCATION:
                mLocationPermission.setChecked(result);
                break;
        }
    }

    private void checkPermissions() {
        int nLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (nLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission.setChecked(true);
        } else {
            mLocationPermission.setChecked(false);
        }
    }
}
