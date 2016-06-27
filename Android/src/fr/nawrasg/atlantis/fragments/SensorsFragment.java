package fr.nawrasg.atlantis.fragments;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.SensorAdapter;
import fr.nawrasg.atlantis.fragments.dialogs.SensorDialogFragment;
import fr.nawrasg.atlantis.type.Room;
import fr.nawrasg.atlantis.type.Sensor;

public class SensorsFragment extends ListFragment {
	private Context mContext;
	private ArrayList<Sensor> mList;
	private ArrayList<Room> mRoomList;
	private Handler mHandler;

	// private Spinner mSpinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mHandler = new Handler();
		View nView = inflater.inflate(R.layout.fragment_sensors, container, false);
		// mSpinner = (Spinner) nView.findViewById(R.id.spSensorsType);
		// ArrayAdapter<CharSequence> nAdapter =
		// ArrayAdapter.createFromResource(
		// mContext, R.array.sensorsType,
		// android.R.layout.simple_spinner_dropdown_item);
		// nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// mSpinner.setAdapter(nAdapter);
		getItems();
		return nView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void getItems() {
		String nURL = App.getFullUrl(mContext) + App.SENSORS + App.getAPI(mContext) + "&get";
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				mList = new ArrayList<Sensor>();
				mRoomList = new ArrayList<Room>();
				try {
					JSONObject nJson = new JSONObject(response.body().string());
					JSONArray arr = nJson.getJSONArray("devices");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject json = arr.getJSONObject(i);
						Sensor nSensor = new Sensor(json.getJSONObject("device"));
						mList.add(nSensor);
						JSONArray array = json.getJSONArray("sensors");
						for(int j = 0; j < array.length(); j++){
							json = array.getJSONObject(j);
							nSensor = new Sensor(json);
							mList.add(nSensor);
						}
					}
					JSONArray nArr = nJson.getJSONArray("rooms");
					for (int i = 0; i < nArr.length(); i++) {
						Room nRoom = new Room(nArr.getJSONObject(i));
						mRoomList.add(nRoom);
					}
				} catch (JSONException e) {
					Toast.makeText(mContext, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						setListAdapter(new SensorAdapter(mContext, mList, mRoomList));
						setItemListener();
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
}
