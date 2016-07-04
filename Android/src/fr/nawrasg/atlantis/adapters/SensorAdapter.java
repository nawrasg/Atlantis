package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    static class SensorHeaderViewHolder{
        @Bind(R.id.lblSensorHeaderName)
        TextView header;

        public SensorHeaderViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class SensorViewHolder{
        @Bind(R.id.imgSensorIcon)
        ImageView image;
        @Bind(R.id.lblSensorName)
        TextView name;
        @Bind(R.id.lblSensorDescription)
        TextView description;

        public SensorViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public SensorAdapter(Context context, ArrayList<Sensor> sensors, ArrayList<Room> rooms){
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
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_sensors_header, parent, false);
            SensorHeaderViewHolder nHolder = new SensorHeaderViewHolder(convertView);
            convertView.setTag(nHolder);
        }
        SensorHeaderViewHolder nHolder = (SensorHeaderViewHolder) convertView.getTag();
        nHolder.header.setText(mList.get(groupPosition).getAlias());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_sensors, parent, false);
            SensorViewHolder nHolder = new SensorViewHolder(convertView);
            convertView.setTag(nHolder);
        }
        SensorViewHolder nHolder = (SensorViewHolder) convertView.getTag();
        nHolder.name.setText(mList.get(groupPosition).getSensor(childPosition).getSensor());
        nHolder.description.setText(mList.get(groupPosition).getSensor(childPosition).getType());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}