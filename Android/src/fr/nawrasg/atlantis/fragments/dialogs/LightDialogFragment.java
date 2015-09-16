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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

public class LightDialogFragment extends DialogFragment implements OnClickListener {
	private Context mContext;
	private Spinner spRoom;
	private ArrayList<Room> mRoomList;
	private Light mLight;
	private ImageView imgRed, imgGreen, imgBlue, imgYellow, imgWhite;
	@Bind(R.id.txtLightDialogName)
	EditText txtName;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_light, null);
		ButterKnife.bind(this, nView);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
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
				new DataPUT(mContext).execute(App.LIGHTS, nVal);
				dismiss();
			}
		}).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
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
		spRoom = (Spinner) view.findViewById(R.id.spinnerLightDialogRoom);
		imgRed = (ImageView) view.findViewById(R.id.imgLightDialogRed);
		imgRed.setOnClickListener(this);
		imgGreen = (ImageView) view.findViewById(R.id.imgLightDialogGreen);
		imgGreen.setOnClickListener(this);
		imgBlue = (ImageView) view.findViewById(R.id.imgLightDialogBlue);
		imgBlue.setOnClickListener(this);
		imgYellow = (ImageView) view.findViewById(R.id.imgLightDialogYellow);
		imgYellow.setOnClickListener(this);
		imgWhite = (ImageView) view.findViewById(R.id.imgLightDialogWhite);
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
			new DataPUT(mContext).execute(App.LIGHTS, "color=" + ((Hue) mLight).getUID() + "&value=" + nColor);
		}
	}
}