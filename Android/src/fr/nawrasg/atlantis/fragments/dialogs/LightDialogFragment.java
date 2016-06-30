package fr.nawrasg.atlantis.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

public class LightDialogFragment extends DialogFragment implements OnClickListener {
	private Context mContext;
	@Bind(R.id.spinnerLightDialogRoom)
	Spinner spRoom;
	private ArrayList<Room> mRoomList;
	private Light mLight;
	@Bind(R.id.imgLightDialogRed)
	ImageView imgRed;
	@Bind(R.id.imgLightDialogGreen)
	ImageView imgGreen;
	@Bind(R.id.imgLightDialogBlue)
	ImageView imgBlue;
	@Bind(R.id.imgLightDialogYellow)
	ImageView imgYellow;
	@Bind(R.id.imgLightDialogWhite)
	ImageView imgWhite;
	@Bind(R.id.txtLightDialogName)
	EditText txtName;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_light, null);
		ButterKnife.bind(this, nView);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(mContext.getString(R.string.fragment_dialog_button_save), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String nVal = "set=" + mLight.getID();
				int nPosition = spRoom.getSelectedItemPosition();
				nVal += "&room=" + ((nPosition == 0) ? -1 : mRoomList.get(nPosition).getID());
				String nName = "";
				try {
					nName = URLEncoder.encode(txtName.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
					return;
				}
				nVal += "&name=" + nName; 
				nVal += "&uid=" + ((Hue)mLight).getUID();
				String nURL = App.getFullUrl(mContext) + App.LIGHTS + App.getAPI(mContext) + "&" + nVal;
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
		imgRed.setOnClickListener(this);
		imgGreen.setOnClickListener(this);
		imgBlue.setOnClickListener(this);
		imgYellow.setOnClickListener(this);
		imgWhite.setOnClickListener(this);
		getSettings();
	}

	private void getSettings() {
		Bundle nBundle = getArguments();
		mLight = nBundle.getParcelable("light");
		txtName.setText(mLight.getName());
		mRoomList = nBundle.getParcelableArrayList("rooms");
		setRoom();
	}

	private void setRoom() {
		if (mRoomList != null && mRoomList.size() > 0) {
			String nID = mLight.getRoom();
			int nPosition = -1;
			List<String> nList = new LinkedList<String>();
			for (int i = 0; i < mRoomList.size(); i++) {
				nList.add(mRoomList.get(i).getRoom());
				if (nID != null && nID.equals(mRoomList.get(i).getID())) {
					nPosition = nList.size() - 1;
				}
			}
			ArrayAdapter<String> nAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item,
					nList);
			nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spRoom.setAdapter(nAdapter);
			if (nPosition != -1)
				spRoom.setSelection(nPosition, true);
		}
	}

	@Override
	public void onClick(View v) {
		String nColor = null;
		switch (v.getId()) {
			case R.id.imgLightDialogRed:
				nColor = "red";
				break;
			case R.id.imgLightDialogGreen:
				nColor = "green";
				break;
			case R.id.imgLightDialogBlue:
				nColor = "blue";
				break;
			case R.id.imgLightDialogYellow:
				nColor = "yellow";
				break;
			case R.id.imgLightDialogWhite:
				nColor = "white";
				break;
			default:
				return;
		}
		if (nColor != null) {
			String nURL = App.getFullUrl(mContext) + App.LIGHTS + App.getAPI(mContext) + "&color=" + ((Hue) mLight).getUID() + "&value=" + nColor;
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
		}
	}
}
