package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.DeviceAdapter;
import fr.nawrasg.atlantis.async.DataDELETE;
import fr.nawrasg.atlantis.async.DataGET;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_devices, container, false);
		c = getActivity();
		getActivity().getActionBar().setIcon(R.drawable.devices);
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
		new DevicesGET(c).execute(App.DEVICES);
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
				new DeviceDELETE(c).execute(App.DEVICES, "id=" + mDevice.getID());
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
	
	private class DevicesGET extends DataGET{

		public DevicesGET(Context context) {
			super(context);
		}
		
		@Override
		protected void onPostExecute(String result) {
			nList = new ArrayList<Device>();
			mUserList = new ArrayList<User>();
			try {
				JSONObject nJson = new JSONObject(result);
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
				Toast.makeText(c, e.getMessage() + " : " + result, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}


	
	private class DeviceDELETE extends DataDELETE{

		public DeviceDELETE(Context context) {
			super(context);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("200")){
				mAdapter.remove(mDevice);
				mAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}
}
