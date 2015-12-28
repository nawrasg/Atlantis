package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Room;
import fr.nawrasg.atlantis.type.Sensor;

public class SensorAdapter extends ArrayAdapter<Sensor> {
	private Context mContext;
	private List<Sensor> mList;
	private List<Room> mRoomList;
	private Sensor mSensor;
	private ImageView imgSensorIcon;
	private TextView lblSensorName;
	private Switch swtSensorToggle;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm");

	public SensorAdapter(Context context, List<Sensor> objects) {
		super(context, 0, objects);
		mContext = context;
		mList = objects;
	}

	public SensorAdapter(Context context, List<Sensor> objects, List<Room> rooms) {
		super(context, 0, objects);
		mContext = context;
		mList = objects;
		mRoomList = rooms;
	}

	@Override
	public int getCount() {
		return (mList == null) ? 0 : mList.size();
	}

	private String getRoom(Sensor sensor) {
		Room nRoom;
		if (mRoomList != null) {
			for (int i = 0; i < mRoomList.size(); i++) {
				nRoom = mRoomList.get(i);
				if (sensor.getRoom().equals(nRoom.getID())) {
					return " (" + nRoom.getRoom() + ")";
				}
			}
		}
		return "";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mSensor = mList.get(position);
		LayoutInflater nInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View nView;

		switch (mSensor.getType()) {
			case "section":
				nView = nInflater.inflate(R.layout.row_sensor_section, parent, false);
				lblSensorName = (TextView) nView.findViewById(R.id.lblSensorName);
				break;
			case "switchBinary":
				nView = nInflater.inflate(R.layout.row_sensor_switch, parent, false);
				imgSensorIcon = (ImageView) nView.findViewById(R.id.imgSensorSwitchIcon);
				lblSensorName = (TextView) nView.findViewById(R.id.lblSensorSwitchName);
				swtSensorToggle = (Switch) nView.findViewById(R.id.swtSensorSwitchToggle);
				swtSensorToggle.setTag(mList.get(position).getSensor());
				swtSensorToggle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String nSensor = (String) v.getTag();
						String nCmd = ((Switch) v).isChecked() ? "on" : "off";
						new DataPUT(mContext).execute(App.SENSORS, "toggle=" + nSensor + "&value=" + nCmd);
					}
				});
				break;
			default:
				nView = nInflater.inflate(R.layout.row_sensors, parent, false);
				imgSensorIcon = (ImageView) nView.findViewById(R.id.imgSensorIcon);
				lblSensorName = (TextView) nView.findViewById(R.id.lblSensorName);
				break;
		}

		if (mSensor.getType().equals("section")) {
			String nVal = "";
			if (mSensor.getAlias().equals("null")) {
				nVal = mSensor.getDevice();
			} else {
				nVal = mSensor.getAlias();
			}
			if (!mSensor.getRoom().equals("null")) {
				nVal += getRoom(mSensor);
			}
			lblSensorName.setText(nVal);
		} else if (!mSensor.getType().equals("section")) {

			switch (mSensor.getType()) {
				case "Electric ":
				case "Electric":
				case "Power":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_electric_consumption) + " " + mSensor.getValue() + " " + mSensor.getUnit());
					imgSensorIcon.setImageResource(R.drawable.ng_lightning);
					break;
				case "Battery":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_battery) + " " + mSensor.getValue() + " " + mSensor.getUnit());
					imgSensorIcon.setImageResource(R.drawable.ng_battery);
					break;
				case "Temperature":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_temperature) + " " + mSensor.getValue() + " " + mSensor.getUnit());
					imgSensorIcon.setImageResource(R.drawable.ng_thermostat);
					break;
				case "Luminiscence":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_lum) + " " + mSensor.getValue() + " " + mSensor.getUnit());
					imgSensorIcon.setImageResource(R.drawable.ng_sun);
					break;
				case "switchBinary":
					imgSensorIcon.setImageResource(R.drawable.ng_switch);
					if (swtSensorToggle != null) {
						swtSensorToggle.setChecked(mSensor.isOn());
					}
					break;
				case "interrupt":
					imgSensorIcon.setImageResource(R.drawable.interrupt32);
					break;
				case "Tamper":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_tamper_date) + " " + getDate(mSensor.getUpdate()));
					imgSensorIcon.setImageResource(R.drawable.ng_alert);
					break;
				case "Door/Window":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_door_status) + " " + (mSensor.getValue().equals("on") ? "Ouvert" : "Fermé"));
					imgSensorIcon.setImageResource(R.drawable.ng_device);
					break;
				case "General purpose":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_last_detected_motion) + " " + getDate(mSensor.getUpdate()));
					imgSensorIcon.setImageResource(R.drawable.ng_device);
					break;
				case "sensorBinary":
					lblSensorName.setText(mContext.getString(R.string.adapter_sensor_item_last_motion) + " " + getDate(mSensor.getUpdate()));
					imgSensorIcon.setImageResource(R.drawable.ng_device);
					break;
				default:
					lblSensorName.setText(mSensor.getValue() + " " + mSensor.getUnit());
					imgSensorIcon.setImageResource(R.drawable.ng_device);
					break;
			}
		}

		return nView;
	}

	private String getDate(String date){
		try {
			Date nDate = new SimpleDateFormat(mContext.getString(R.string.app_datetime_format)).parse(date);
			return mDateFormat.format(nDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
