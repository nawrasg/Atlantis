package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.spinner.ConnectionAdapter;
import fr.nawrasg.atlantis.adapters.spinner.DeviceTypeAdapter;
import fr.nawrasg.atlantis.async.DataPOST;
import fr.nawrasg.atlantis.other.CheckConnection;

public class DevicesAddFragment extends Fragment {
	private Context mContext;
	@Bind(R.id.txtDevicesAddName)
	EditText txtTitle;
	@Bind(R.id.txtDevicesAddIp)
	EditText txtIP;
	@Bind(R.id.txtDevicesAddMac)
	EditText txtMAC;
	private Spinner spType, spConnection;
	private WifiManager mWM;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_devices_add, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		spType = (Spinner) view.findViewById(R.id.spDevicesAddType);
		spConnection = (Spinner) view.findViewById(R.id.spDevicesAddConnection);
		String[] nList = { "ethernet", "wifi" };
		spConnection.setAdapter(new ConnectionAdapter(mContext, nList));
		String[] nList2 = { "windows", "linux", "imprimante", "smartphone", "autre" };
		spType.setAdapter(new DeviceTypeAdapter(mContext, nList2));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_devices_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemDevicesAddSave:
				save();
				return true;
			case R.id.itemDevicesAddThis:
				txtMAC.setText(getMacAddress());
				txtIP.setText(getIpAddress());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void save() {
		if (txtTitle.getText().toString().equals("") || txtIP.getText().toString().equals("")
				|| txtMAC.getText().toString().equals("")) {
			return;
		}
		try {
			String nDeviceTitle = URLEncoder.encode(txtTitle.getText().toString(), "UTF-8");
			String nDeviceIp = URLEncoder.encode(txtIP.getText().toString(), "UTF-8");
			String nDeviceMac = URLEncoder.encode(txtMAC.getText().toString(), "UTF-8");
			String nDeviceType = (String) spType.getItemAtPosition(spType.getSelectedItemPosition());
			String nDeviceConnection = (String) spConnection.getItemAtPosition(spConnection.getSelectedItemPosition());
			String nParams = "title=" + nDeviceTitle + "&ip=" + nDeviceIp + "&mac=" + nDeviceMac + "&type=" + nDeviceType
					+ "&connection=" + nDeviceConnection;
			new DevicePOST(mContext).execute(App.DEVICES, nParams);
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private String getMacAddress() {
		mWM = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo nWI = mWM.getConnectionInfo();
		return nWI.getMacAddress();
	}

	private String getIpAddress() {
		CheckConnection nConnection = new CheckConnection(mContext);
		if (nConnection.checkConnection() == CheckConnection.TYPE_WIFI) {
			int ipAddress = mWM.getConnectionInfo().getIpAddress();
			return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
					(ipAddress >> 24 & 0xff));
		}
		return "";
	}

	private class DevicePOST extends DataPOST {

		public DevicePOST(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			((MainFragmentActivity)getActivity()).loadFragment(new ConnectedDevicesFragment(), true);
		}

	}
}
