package fr.nawrasg.atlantis.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Device;

public class DeviceDialogFragment extends DialogFragment{
	private Context mContext;
	private Device mDevice;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_device, null);
		ButterKnife.bind(this, nView);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(mContext.getString(R.string.fragment_dialog_button_end), new DialogInterface.OnClickListener() {
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
		loadSettings();
	}
	
	private void loadSettings(){
		Bundle nBundle = getArguments();
		mDevice = nBundle.getParcelable("device");
	}

	@OnClick(R.id.btnDeviceDialogWol)
	public void wakeDevice(){
		try {
			String nDeviceMac = URLEncoder.encode(mDevice.getMac(), "UTF-8");
			String nURL = App.getFullUrl(mContext) + App.DEVICES + App.getAPI(mContext) + "&wol=" + nDeviceMac;
			Request nRequest = new Request.Builder()
					.url(nURL)
					.put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
					.build();
			App.httpClient.newCall(nRequest).enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {

				}

				@Override
				public void onResponse(Response response) throws IOException {

				}
			});
			dismiss();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
