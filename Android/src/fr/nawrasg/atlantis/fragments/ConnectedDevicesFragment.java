package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.DeviceAdapter;
import fr.nawrasg.atlantis.fragments.dialogs.DeviceDialogFragment;
import fr.nawrasg.atlantis.fragments.dialogs.DeviceInfoDialogFragment;
import fr.nawrasg.atlantis.listener.ShakeListener;
import fr.nawrasg.atlantis.listener.ShakeListener.OnShakeListener;
import fr.nawrasg.atlantis.type.Device;
import fr.nawrasg.atlantis.type.User;

public class ConnectedDevicesFragment extends ListFragment {
	private Context c;
	private SensorManager nSensorManager;
	private Sensor nAccelerometer;
	private ShakeListener nShakeListener;
	private List<Device> nList;
	private DeviceAdapter mAdapter;
	private Device mDevice;
	private ArrayList<User> mUserList;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_devices, container, false);
		c = getActivity();
		mHandler = new Handler();
		getActivity().getActionBar().setIcon(R.drawable.ng_connected);
		nSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		nAccelerometer = nSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		nShakeListener = new ShakeListener();
		nShakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {
				if (count == 1) {
					getItems();
				}
			}
		});
		setHasOptionsMenu(true);
		return nView;
	}
	
	private void getItems(){
		String nURL = App.getFullUrl(c) + App.DEVICES + "?api=" + App.getAPI(c);
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				nList = new ArrayList<Device>();
				mUserList = new ArrayList<User>();
				try {
					JSONObject nJson = new JSONObject(response.body().string());
					JSONArray nDeviceArr = nJson.getJSONArray("devices");
					for (int i = 0; i < nDeviceArr.length(); i++) {
						JSONObject json = nDeviceArr.getJSONObject(i);
						Device nDevice = new Device(json);
						nList.add(nDevice);
					}
					mAdapter = new DeviceAdapter(c, nList);
					setListAdapter(mAdapter);
					setListListeners();
					JSONArray nUserArr = nJson.getJSONArray("users");
					mUserList.add(new User());
					for(int i = 0; i < nUserArr.length(); i++){
						mUserList.add(new User(nUserArr.getJSONObject(i)));
					}
				} catch (JSONException e) {
					Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		nSensorManager.registerListener(nShakeListener, nAccelerometer, SensorManager.SENSOR_DELAY_UI);
		getItems();
	}

	@Override
	public void onPause() {
		nSensorManager.unregisterListener(nShakeListener);
		super.onPause();
	}

	private void setListListeners(){
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				return false;
			}
		});
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_devices, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.itemDevicesAdd:
				((MainFragmentActivity)getActivity()).loadFragment(new DevicesAddFragment(), true);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		 MenuInflater nMI = getActivity().getMenuInflater();
		 nMI.inflate(R.menu.context_devices, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		mDevice = mAdapter.getItem(position);
		switch (item.getItemId()) {
			case R.id.menuDeviceInfo:
				openDeviceInfo(mDevice);
				return true;
			case R.id.menuDeviceDelete:
				deleteItem(mDevice);
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void openDeviceInfo(Device device){
		DeviceInfoDialogFragment nDeviceDialog = new DeviceInfoDialogFragment();
		Bundle nArgs = setArgs(device);
		nDeviceDialog.setArguments(nArgs);
		nDeviceDialog.show(getFragmentManager(), "device");
	}
	
	private void openDeviceCommands(Device device){
		DeviceDialogFragment nDeviceDialog = new DeviceDialogFragment();
		Bundle nArgs = setArgs(device);
		nDeviceDialog.setArguments(nArgs);
		nDeviceDialog.show(getFragmentManager(), "device");		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Device nDevice = mAdapter.getItem(position);
		if (nDevice.getNom().equals("Serveur")) {

		} else {
			openDeviceCommands(nDevice);
		}
	}
	
	
	private Bundle setArgs(Device device) {
		Bundle nBundle = new Bundle();
		nBundle.putParcelable("device", device);
		nBundle.putParcelableArrayList("users", mUserList);
		return nBundle;
	}

	private void deleteItem(Device device){
		String nURL = App.getFullUrl(c) + App.DEVICES + "?api=" + App.getAPI(c) + "&id=" + device.getID();
		Request nRequest = new Request.Builder().url(nURL).delete().build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				if(response.body().string().equals("200")){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mAdapter.remove(mDevice);
							mAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}
}
