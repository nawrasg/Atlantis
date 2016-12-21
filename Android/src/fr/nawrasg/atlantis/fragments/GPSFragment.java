package fr.nawrasg.atlantis.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.fragments.preferences.GeoPreferenceFragment;

public class GPSFragment extends Fragment {
    private Context mContext;
    @Bind(R.id.txtGPScoord)
    EditText txtCoord;
    @Bind(R.id.txtGPSradius)
    EditText txtRadius;
    private LocationManager nLM;
    @Bind(R.id.cbGPSon)
    CheckBox cbGeo;

    private fr.nawrasg.atlantis.type.Location mLocation;

    private static final String ATLANTIS_GEO_ALERT = "fr.nawrasg.atlantis.geo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View nView = inflater.inflate(R.layout.fragment_gps, container, false);
        ButterKnife.bind(this, nView);
        mLocation = new fr.nawrasg.atlantis.type.Location(mContext);
        nLM = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Intent nIntent = new Intent(ATLANTIS_GEO_ALERT);
        boolean nActivate = (PendingIntent.getBroadcast(mContext, 0, nIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (nActivate)
            cbGeo.setChecked(true);
        cbGeo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setProximityAlert(isChecked);
            }
        });
        getSettings();
        if (getActivity().findViewById(R.id.main_fragment2) == null) {
            setHasOptionsMenu(true);
        }
        verifyLocationPermission();
        return nView;
    }

    private void verifyLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int nLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (nLocationPermission == PackageManager.PERMISSION_DENIED) {
                AlertDialog nDialog = new AlertDialog.Builder(mContext).create();
                nDialog.setTitle(mContext.getString(R.string.app_name));
                nDialog.setMessage(mContext.getString(R.string.fragment_gps_permission_warning));
                nDialog.setIcon(R.drawable.home);
                nDialog.setCancelable(false);
                nDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).loadFragment(new WidgetsFragment(), true);
                    }
                });
                nDialog.show();
            }
        }
    }

    @OnClick(R.id.btnGPSClear)
    public void clear() {
        txtCoord.getText().clear();
        txtRadius.getText().clear();
        mLocation.setHomeCoordinates(0, 0);
        mLocation.setHomeRadius(0);
        if (cbGeo.isChecked()) {
            cbGeo.setChecked(false);
        }
    }

    @OnClick(R.id.btnGPSRadius)
    public void setRadius() {
        mLocation.setHomeRadius(Float.parseFloat(txtRadius.getText().toString()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_gps, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemGPSOptions:
                ((MainActivity) getActivity()).loadFragment(new GeoPreferenceFragment(), true);
                return true;
            case R.id.itemGPSMap:
                ((MainActivity) getActivity()).loadFragment(new MapsFragment(), true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSettings() {
        float nLong = mLocation.getHomeLongitude();
        float nLat = mLocation.getHomeLatitude();
        if (nLong != 0 && nLat != 0) {
            txtCoord.setText(nLat + " / " + nLong);
        }
        int nRadius = (int) mLocation.getHomeRadius();
        if (nRadius != 0)
            txtRadius.setText(nRadius + "");
    }


    @OnClick(R.id.btnGPSPosition)
    public void getCoord() {
        nLM.requestSingleUpdate(mLocation.getProvider(), new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                float nLong = (float) location.getLongitude();
                float nLat = (float) location.getLatitude();
                txtCoord.setText(nLat + " / " + nLong);
                mLocation.setHomeCoordinates(nLat, nLong);
            }
        }, null);

    }

    private void setProximityAlert(boolean x) {
        double nLong = mLocation.getHomeLongitude();
        double nLat = mLocation.getHomeLatitude();
        float nRadius = mLocation.getHomeRadius();
        // check if zero or not
        Intent nIntent = new Intent(ATLANTIS_GEO_ALERT);
        PendingIntent nPI = PendingIntent.getBroadcast(mContext, 0, nIntent, 0);
        if (x) {
            nLM.addProximityAlert(nLat, nLong, nRadius, -1, nPI);
        } else {
            // PendingIntent nPI = PendingIntent.getBroadcast(nContext, 0,
            // nIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            nPI.cancel();
            nLM.removeProximityAlert(nPI);
        }
    }
}
