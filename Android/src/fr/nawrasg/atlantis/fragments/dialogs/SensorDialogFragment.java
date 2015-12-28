package fr.nawrasg.atlantis.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.type.Room;
import fr.nawrasg.atlantis.type.Sensor;

public class SensorDialogFragment extends DialogFragment{
	@Bind(R.id.txtSensorDialogAlias)
	EditText txtAlias;
	private Spinner spRoom;
	private CheckBox cbHistory, cbIgnore;
	private Context mContext;
	private String mDevice;
	private ArrayList<Room> mRoomList;

	@Bind(R.id.lblSensorDialogRoom)
	TextView lblRoom;
	@Bind(R.id.lblSensorDialogAlias)
	TextView lblAlias;
	@Bind(R.id.lblSensorDialogTimestamp)
	TextView lblTimestamp;
	private Sensor mSensor;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View nView = inflater.inflate(R.layout.fragment_dialog_sensor, null);
		ButterKnife.bind(this, nView);
		mDevice = getArguments().getString("device");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mDevice)
               .setPositiveButton(mContext.getString(R.string.fragment_dialog_button_save), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       if(mSensor.getType().equals("section")){
                    	   String nVal = "type=section";
									try {
										nVal += "&device=" +  mSensor.getDevice();
										nVal += "&alias=" + URLEncoder.encode(txtAlias.getText().toString(), "UTF-8");
										int nPosition = spRoom.getSelectedItemPosition();
										nVal += "&room=" + ((nPosition == 0) ? -1 : mRoomList.get(nPosition).getID());
									} catch (UnsupportedEncodingException e) {
										Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
                    	   new SensorPOST(mContext).execute(App.SENSORS, "set&" + nVal);
                       }else{
                    	   String nVal = "type=sensor";
                    	   nVal += "&sensor=" + mSensor.getSensor();
                    	   nVal += "&history=" + (cbHistory.isChecked() ? 1 : 0);
                    	   nVal += "&ignore=" + (cbIgnore.isChecked() ? 1 : 0);
                    	   new SensorPOST(mContext).execute(App.SENSORS, "set&" + nVal);
                       }
                       dismiss();
                   }
               })
               .setNegativeButton(mContext.getString(R.string.fragment_dialog_button_cancel), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
               })
               .setView(nView);
        onViewCreated(nView, savedInstanceState);
        return builder.create();
        
	}
		
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		spRoom = (Spinner)view.findViewById(R.id.spinnerSensorDialogRoom);
		cbHistory = (CheckBox)view.findViewById(R.id.cbSensorDialogHistory);
		cbIgnore = (CheckBox)view.findViewById(R.id.cbSensorDialogIgnore);
		getSettings();
	}
	
	private void setRoom(){
		if(mRoomList != null && mRoomList.size() > 0){
			String nID = mSensor.getRoom();
			int nPosition = -1;
			List<String> nList = new LinkedList<String>();
			for(int i = 0; i < mRoomList.size(); i++){
				nList.add(mRoomList.get(i).getRoom());
				if(!nID.equals("null") && nID.equals(mRoomList.get(i).getID())){
					nPosition = nList.size() - 1; 
				}
			}
			ArrayAdapter<String> nAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, nList);
			nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spRoom.setAdapter(nAdapter);
			if(nPosition != -1) spRoom.setSelection(nPosition, true);			
		}
	}
	
	private void setView(boolean section){
		if(section){
			lblAlias.setVisibility(View.VISIBLE);
			txtAlias.setVisibility(View.VISIBLE);
			lblRoom.setVisibility(View.VISIBLE);
			spRoom.setVisibility(View.VISIBLE);
			lblTimestamp.setVisibility(View.GONE);
			cbHistory.setVisibility(View.GONE);
			cbIgnore.setVisibility(View.GONE);
		}else{
			lblAlias.setVisibility(View.GONE);
			txtAlias.setVisibility(View.GONE);
			lblRoom.setVisibility(View.GONE);
			spRoom.setVisibility(View.GONE);
			lblTimestamp.setVisibility(View.VISIBLE);
			cbHistory.setVisibility(View.VISIBLE);
			cbIgnore.setVisibility(View.VISIBLE);
		}
	}
	
	private void getSettings(){
		Bundle nBundle = getArguments();
		mSensor = nBundle.getParcelable("sensor");
		if(mSensor.getType().equals("section")){
			setView(true);
			if(!mSensor.getAlias().equals("null")) txtAlias.setText(mSensor.getAlias());
			mRoomList = nBundle.getParcelableArrayList("rooms");
			setRoom();
		}else{
			setView(false);			
			lblTimestamp.setText(mContext.getString(R.string.app_last_connection) + " " + mSensor.getUpdate());
			cbHistory.setChecked(mSensor.getHistory());
			cbIgnore.setChecked(mSensor.getIgnore());
		}
	}
	
	private class SensorPOST extends DataGET{

		public SensorPOST(Context context) {
			super(context);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
		}
		
	}
	
}