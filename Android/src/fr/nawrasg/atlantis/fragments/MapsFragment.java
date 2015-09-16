package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;
import fr.nawrasg.atlantis.type.GPS;

public class MapsFragment extends Fragment {
	private Context mContext;
	private GoogleMap mMap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		View nView = inflater.inflate(R.layout.fragment_maps, container, false);
		MapFragment nFragment = getMapFragment();
		if (nFragment == null) {
			Toast.makeText(mContext, "Impossible de charger la carte !", Toast.LENGTH_SHORT).show();
		} else {
			mMap = nFragment.getMap();
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
		}
		setHasOptionsMenu(true);
		setHome(true);
		return nView;
	}
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_maps, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemMapsRefresh:
				new UsersGET(mContext).execute(App.GEO);
				return true;
			case R.id.itemMapsRequest:
				new DataPOST(mContext).execute(App.GEO);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setHome(boolean zoom) {
		float nLat = App.getFloat(mContext, "homeLat");
		float nLong = App.getFloat(mContext, "homeLong");
		LatLng nHomeCoords;
		if (nLat != 0 && nLong != 0) {
			nHomeCoords = new LatLng(nLat, nLong);
			MarkerOptions nMarker = new MarkerOptions().position(nHomeCoords).title("Atlantis");
			nMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			mMap.addMarker(nMarker);
			if (zoom) {
				mMap.moveCamera(CameraUpdateFactory.newLatLng(nHomeCoords));
				mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
			}
		}
	}

	private MapFragment getMapFragment() {
		FragmentManager fm;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			fm = getFragmentManager();
		} else {
			fm = getChildFragmentManager();
		}
		return (MapFragment) fm.findFragmentById(R.id.map);
	}

	private class UsersGET extends DataGET {

		public UsersGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject nJson = new JSONObject(result);
				JSONArray nArr = nJson.getJSONArray("positions");
				mMap.clear();
				setHome(false);
				for (int i = 0; i < nArr.length(); i++) {
					GPS nGPS = new GPS(nArr.getJSONObject(i));
					LatLng nCoords = new LatLng(nGPS.getLatitude(), nGPS.getLongitude());
					mMap.moveCamera(CameraUpdateFactory.newLatLng(nCoords));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
					MarkerOptions nMarker = new MarkerOptions().position(nCoords).title(
							/*nGPS.getUser() + */" (" + nGPS.getTimestamp() + ")");
					mMap.addMarker(nMarker);
				}
			} catch (Exception e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
		}

	}

}
