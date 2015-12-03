package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.preferences.GeoPreferenceFragment;

public class GPSFragment extends Fragment {
	private Context nContext;
	@Bind(R.id.txtGPScoord)
	EditText txtCoord;
	@Bind(R.id.txtGPSradius)
	EditText txtRadius;
	private LocationManager nLM;
	private CheckBox cbGeo;

	private static final String ATLANTIS_GEO_ALERT = "fr.nawrasg.atlantis.geo";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		nContext = getActivity();
		getActivity().getActionBar().setIcon(R.drawable.ng_satellite);
		View nView = inflater.inflate(R.layout.layout_gps, container, false);
		ButterKnife.bind(this, nView);
		nLM = (LocationManager) nContext.getSystemService(Context.LOCATION_SERVICE);
		cbGeo = (CheckBox) nView.findViewById(R.id.cbGPSon);
		Intent nIntent = new Intent(ATLANTIS_GEO_ALERT);
		boolean nActivate = (PendingIntent.getBroadcast(nContext, 0, nIntent, PendingIntent.FLAG_NO_CREATE) != null);
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
		return nView;
	}

	@OnClick(R.id.btnGPSClear)
	public void clear(){
		txtCoord.getText().clear();
		txtRadius.getText().clear();
		App.setFloat(nContext, "homeLong", 0);
		App.setFloat(nContext, "homeLat", 0);
		App.setFloat(nContext, "homeRadius", 0);
		if (cbGeo.isChecked()) {
			cbGeo.setChecked(false);
		}
	}

	@OnClick(R.id.btnGPSRadius)
	public void setRadius(){
		App.setFloat(nContext, "homeRadius", Float.parseFloat(txtRadius.getText().toString()));
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
				((MainFragmentActivity) getActivity()).loadFragment(new GeoPreferenceFragment(), true);
				return true;
			case R.id.itemGPSMap:
				((MainFragmentActivity) getActivity()).loadFragment(new MapsFragment(), true);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getSettings() {
		float nLong = App.getFloat(nContext, "homeLong");
		float nLat = App.getFloat(nContext, "homeLat");
		if (nLong != 0 && nLat != 0) {
			txtCoord.setText(nLat + " / " + nLong);
		}
		int nRadius = (int) App.getFloat(nContext, "homeRadius");
		if (nRadius != 0)
			txtRadius.setText(nRadius + "");
	}

	@OnClick(R.id.btnGPSPosition)
	public void getCoord() {
		nLM.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {

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
				App.setFloat(nContext, "homeLong", nLong);
				App.setFloat(nContext, "homeLat", nLat);
			}
		}, null);

	}

	private void setProximityAlert(boolean x) {
		double nLong = App.getFloat(nContext, "homeLong");
		double nLat = App.getFloat(nContext, "homeLat");
		float nRadius = App.getFloat(nContext, "homeRadius");
		// check if zero or not
		Intent nIntent = new Intent(ATLANTIS_GEO_ALERT);
		PendingIntent nPI = PendingIntent.getBroadcast(nContext, 0, nIntent, 0);
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
