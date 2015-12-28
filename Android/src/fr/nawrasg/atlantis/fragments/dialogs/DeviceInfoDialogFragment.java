package fr.nawrasg.atlantis.fragments.dialogs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.UserAdapter;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Device;
import fr.nawrasg.atlantis.type.User;

public class DeviceInfoDialogFragment extends DialogFragment {
	private Context mContext;
	@Bind(R.id.txtDeviceDialogTitle)
	EditText txtTitle;
	@Bind(R.id.txtDeviceDialogIp)
	EditText txtIP;
	private Spinner spUser;
	private Device mDevice;
	private ArrayList<User> mUserList;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_device_info, null);
		ButterKnife.bind(this, nView);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(mContext.getString(R.string.fragment_dialog_button_save), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					String nDeviceTitle = URLEncoder.encode(txtTitle.getText().toString(), "UTF-8");
					String nDeviceIp = URLEncoder.encode(txtIP.getText().toString(), "UTF-8");
					String nDeviceMac = URLEncoder.encode(mDevice.getMac(), "UTF-8");
					int nPosition = spUser.getSelectedItemPosition();
					String nDeviceOwner = URLEncoder.encode(mUserList.get(nPosition).getName(), "UTF-8");
					String nURL = "title=" + nDeviceTitle + "&ip=" + nDeviceIp + "&mac=" + nDeviceMac + "&user=" + nDeviceOwner;
					new DevicePUT(mContext).execute(App.DEVICES, nURL);
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				dismiss();
			}
		}).setNegativeButton(mContext.getString(R.string.fragment_dialog_button_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dismiss();
			}
		}).setView(nView);
		onViewCreated(nView, savedInstanceState);
		return builder.create();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		spUser = (Spinner) view.findViewById(R.id.spDeviceDialogUser);
		getSettings();

	}

	private void getSettings() {
		Bundle nBundle = getArguments();
		mDevice = nBundle.getParcelable("device");
		mUserList = nBundle.getParcelableArrayList("users");
		txtTitle.setText(mDevice.getNom());
		txtIP.setText(mDevice.getIP());
		spUser.setAdapter(new UserAdapter(mContext, mUserList));
		for (int i = 0; i < mUserList.size(); i++) {
			if (mUserList.get(i).getName().equals(mDevice.getOwner())) {
				spUser.setSelection(i);
				break;
			}
		}
	}

	private class DevicePUT extends DataPUT {

		public DevicePUT(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismiss();
		}

	}
}
