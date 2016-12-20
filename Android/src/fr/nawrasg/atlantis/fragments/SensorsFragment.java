package fr.nawrasg.atlantis.fragments;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.adapters.SensorAdapter;
import fr.nawrasg.atlantis.fragments.dialogs.SensorDialogFragment;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Room;
import fr.nawrasg.atlantis.type.Sensor;

public class SensorsFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeSensors)
    SwipeRefreshLayout mSwipeLayout;
    private Context mContext;
    private ArrayList<Sensor> mList;
    private ArrayList<Room> mRoomList;
    private Handler mHandler;
    private SensorAdapter mAdapter;
    @Bind(R.id.listSensorsList)
    ExpandableListView listSensors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mHandler = new Handler();
        View nView = inflater.inflate(R.layout.fragment_sensors, container, false);
        ButterKnife.bind(this, nView);
        mSwipeLayout.setOnRefreshListener(this);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getItems();
        ((MainActivity)getActivity()).setProgressBar(true);
        getStatus();
    }

    private void getItems() {
        ContentResolver nResolver = mContext.getContentResolver();
        Cursor nRoomsCursor = nResolver.query(AtlantisContract.Rooms.CONTENT_URI, null, null, null, null);
        if (nRoomsCursor.moveToFirst()) {
            mRoomList = new ArrayList<>();
            do {
                Room nRoom = new Room(nRoomsCursor);
                mRoomList.add(nRoom);
            } while (nRoomsCursor.moveToNext());
        }

        mList = new ArrayList<>();
        Cursor nDevicesCursor = nResolver.query(AtlantisContract.SensorsDevices.CONTENT_URI, null, null, null, null);
        if (nDevicesCursor.moveToFirst()) {
            do {
                Sensor nSensor = new Sensor(nDevicesCursor, true);
                String[] nSelectionArgs = {nSensor.getDevice()};
                Cursor nSensorsCursor = nResolver.query(AtlantisContract.Sensors.CONTENT_URI, null, "device = ?", nSelectionArgs, null);
                nSensor.addSensors(nSensorsCursor);
                mList.add(nSensor);
            } while (nDevicesCursor.moveToNext());
            mAdapter = new SensorAdapter(mContext, mList, mRoomList);
            listSensors.setAdapter(mAdapter);
        }
    }

    public void getStatus() {
        String nURL = App.getUri(mContext, App.SENSORS);
        Request nRequest = new Request.Builder().url(nURL).build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                try {
                    JSONObject nJson = new JSONObject(response.body().string());
                    JSONArray arr = nJson.getJSONArray("devices");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject json = arr.getJSONObject(i);
                        Sensor nSensor = new Sensor(json.getJSONObject("device"));
                        ArrayList<Sensor> nList = new ArrayList<Sensor>();
                        JSONArray array = json.getJSONArray("sensors");
                        for (int j = 0; j < array.length(); j++) {
                            json = array.getJSONObject(j);
                            Sensor nChildSensor = new Sensor(json);
                            nList.add(nChildSensor);
                        }
                        int nIndex = mList.indexOf(nSensor);
                        if(nIndex >= 0){
                            mList.get(nIndex).update(nList);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
                        ((MainActivity)getActivity()).setProgressBar(false);
                        //setItemListener();
                    }
                });
            }
        });
    }

    private void setItemListener() {
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor nSensor = mList.get(position);
                Log.d("Nawras", nSensor.getSensor() + " " + nSensor.getID());
                Bundle nArgs;
                nArgs = setArgs(nSensor);
                DialogFragment nDialog;
                nDialog = new SensorDialogFragment();
                nDialog.setArguments(nArgs);
                nDialog.show(getFragmentManager(), "sensor");
            }
        });
    }

    private Bundle setArgs(Sensor sensor) {
        Bundle nBundle = new Bundle();
        nBundle.putParcelableArrayList("rooms", mRoomList);
        nBundle.putParcelable("sensor", sensor);
        return nBundle;
    }

    @Override
    public void onRefresh() {
        getStatus();
    }
}
