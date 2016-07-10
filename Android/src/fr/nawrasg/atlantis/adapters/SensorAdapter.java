package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Room;
import fr.nawrasg.atlantis.type.Sensor;

/**
 * Created by Nawras on 04/07/2016.
 */

public class SensorAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Sensor> mList;
    private ArrayList<Room> mRoomList;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm");

    static class SensorHeaderViewHolder {
        @Bind(R.id.lblSensorHeaderName)
        TextView header;

        public SensorHeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class SensorViewHolder {
        @Bind(R.id.imgSensorIcon)
        ImageView image;
        @Bind(R.id.lblSensorName)
        TextView name;
        @Bind(R.id.lblSensorDescription)
        TextView description;

        public SensorViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class SwitchSensorViewHolder {
        @Bind(R.id.lblSensorSwitchName)
        TextView name;
        @Bind(R.id.lblSensorSwitchDescription)
        TextView description;
        @Bind(R.id.swtSensorSwitchToggle)
        Switch toggle;

        public SwitchSensorViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public SensorAdapter(Context context, ArrayList<Sensor> sensors, ArrayList<Room> rooms) {
        mContext = context;
        mList = sensors;
        mRoomList = rooms;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getSensorsCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getSensor(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return Long.parseLong(mList.get(groupPosition).getID());
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.parseLong(mList.get(groupPosition).getSensor(childPosition).getID());
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_sensors_header, parent, false);
            SensorHeaderViewHolder nHolder = new SensorHeaderViewHolder(convertView);
            convertView.setTag(nHolder);
        }
        SensorHeaderViewHolder nHolder = (SensorHeaderViewHolder) convertView.getTag();
        Sensor nSensor = mList.get(groupPosition);
        String nHeader = nSensor.getAlias();
        if (!nSensor.getRoom().equals("null")) {
            nHeader += getRoom(nSensor);
        }
        nHolder.header.setText(nHeader);
        if (nSensor.getSensorsCount() > 0) {
            nHolder.header.setCompoundDrawablePadding(20);
            if (isExpanded) {
                nHolder.header.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_arrow_down, 0, 0, 0);
            } else {
                nHolder.header.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_arrow_right, 0, 0, 0);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Sensor nSensor = mList.get(groupPosition).getSensor(childPosition);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (nSensor.getType().equals("switchBinary")) {
            convertView = inflater.inflate(R.layout.row_sensor_switch, parent, false);
            SwitchSensorViewHolder nHolder = new SwitchSensorViewHolder(convertView);
            nHolder.toggle.setTag(nSensor.getSensor());
            nHolder.toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nSensorLabel = (String) v.getTag();
                    String nCmd = ((Switch) v).isChecked() ? "on" : "off";
                    String nURL = App.getUri(mContext, App.SENSORS) + "&toggle=" + nSensorLabel + "&value=" + nCmd;
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
            });
            nHolder.name.setText("");
            nHolder.description.setText("");
            nHolder.toggle.setChecked(nSensor.isOn());
        } else {
            convertView = inflater.inflate(R.layout.row_sensors, parent, false);
            SensorViewHolder nHolder = new SensorViewHolder(convertView);
            nHolder.name.setText(nSensor.getSensor());
            switch (nSensor.getType()) {
                case "Electric ":
                case "Electric":
                case "Power":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_electric_consumption) + " " + nSensor.getValue() + " " + nSensor.getUnit());
                    nHolder.image.setImageResource(R.drawable.ng_lightning);
                    break;
                case "Battery":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_battery) + " " + nSensor.getValue() + " " + nSensor.getUnit());
                    nHolder.image.setImageResource(R.drawable.ng_battery);
                    break;
                case "Temperature":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_temperature) + " " + nSensor.getValue() + " " + nSensor.getUnit());
                    nHolder.image.setImageResource(R.drawable.ng_thermostat);
                    break;
                case "Luminiscence":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_lum) + " " + nSensor.getValue() + " " + nSensor.getUnit());
                    nHolder.image.setImageResource(R.drawable.ng_sun);
                    break;

                case "Tamper":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_tamper_date) + " " + getDate(nSensor.getUpdate()));
                    nHolder.image.setImageResource(R.drawable.ng_alert);
                    break;
                case "Door/Window":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_door_status) + " " + (nSensor.getValue().equals("on") ? "Ouvert" : "Fermé"));
                    nHolder.image.setImageResource(R.drawable.ng_device);
                    break;
                case "General purpose":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_last_detected_motion) + " " + getDate(nSensor.getUpdate()));
                    nHolder.image.setImageResource(R.drawable.ng_device);
                    break;
                case "sensorBinary":
                    nHolder.name.setText(mContext.getString(R.string.adapter_sensor_item_last_motion) + " " + getDate(nSensor.getUpdate()));
                    nHolder.image.setImageResource(R.drawable.ng_device);
                    break;
                default:
                    nHolder.name.setText(nSensor.getValue() + " " + nSensor.getUnit());
                    nHolder.image.setImageResource(R.drawable.ng_device);
                    break;
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private String getDate(String date) {
        try {
            Date nDate = new SimpleDateFormat(mContext.getString(R.string.app_datetime_format)).parse(date);
            return mDateFormat.format(nDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
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
}