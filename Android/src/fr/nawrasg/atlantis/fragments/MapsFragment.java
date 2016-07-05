package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.GPS;

public class MapsFragment extends Fragment {
    private Context mContext;
    private GoogleMap mMap;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mHandler = new Handler();
        View nView = inflater.inflate(R.layout.fragment_maps, container, false);
        MapFragment nFragment = getMapFragment();
        if (nFragment == null) {
            Toast.makeText(mContext, getResources().getString(R.string.fragment_maps_no_maps), Toast.LENGTH_SHORT).show();
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
                getUsers();
                return true;
            case R.id.itemMapsRequest:
                post();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void post() {
        String nURL = App.getFullUrl(mContext) + App.GEO + "?api=" + App.getAPI(mContext);
        Request nRequest = new Request.Builder()
                .url(nURL)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private void setHome(boolean zoom) {
        float nLat = App.getFloat(mContext, "homeLat");
        float nLong = App.getFloat(mContext, "homeLong");
        LatLng nHomeCoords;
        if (nLat != 0 && nLong != 0) {
            nHomeCoords = new LatLng(nLat, nLong);
            MarkerOptions nMarker = new MarkerOptions().position(nHomeCoords).title(getResources().getString(R.string.app_name));
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

    private void addMarkers(JSONObject positions) throws JSONException {
        mMap.clear();
        JSONArray nArr = positions.getJSONArray("positions");
        for (int i = 0; i < nArr.length(); i++) {
            GPS nGPS = new GPS(nArr.getJSONObject(i));
            LatLng nCoords = new LatLng(nGPS.getLatitude(), nGPS.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(nCoords));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            MarkerOptions nMarker = new MarkerOptions().position(nCoords).title(nGPS.getUser() + " (" + nGPS.getTimestamp() + ")");
            mMap.addMarker(nMarker);
        }
        setHome(true);
    }

    private void getUsers() {
        String nURL = App.getFullUrl(mContext) + App.GEO + "?api=" + App.getAPI(mContext);
        Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final JSONObject nJson = new JSONObject(response.body().string());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                addMarkers(nJson);
                            } catch (JSONException e) {
                                Log.e("Atlantis", e.toString());
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }
}
