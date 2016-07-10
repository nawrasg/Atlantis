package fr.nawrasg.atlantis.type;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Sensor extends PDevice implements Parcelable {
    private String mID, mSensor, mDevice, mRoom, mType, mUnit, mDate, mTime, mValue, mUpdate, mAlias;
    private int mHistory, mIgnore;
    private ArrayList<Sensor> mSensorsList;

    public Sensor(JSONObject json) {
        try {
            mDevice = json.optString("device");
            mType = json.optString("type");
            mID = json.optString("id");
            if (!mType.equals("section")) {
                mSensor = json.optString("sensor");
                mUnit = json.optString("unit");
                mDate = json.optString("date");
                mTime = json.optString("time");
                mValue = json.optString("value");
                mUpdate = json.optString("update");
                mHistory = json.optInt("history");
                mIgnore = json.optInt("ignore");
                mAlias = json.optString("alias");
            } else {
                mRoom = json.optString("room");
                mAlias = json.optString("alias");
            }
        } catch (Exception e) {
            Log.e("Atlantis", e.toString());
        }
        mSensorsList = new ArrayList<>();
    }

    public Sensor(Cursor cursor, boolean section) {
        mID = cursor.getString(cursor.getColumnIndex("id"));
        mDevice = cursor.getString(cursor.getColumnIndex("device"));
        if (section) {
            mRoom = cursor.getString(cursor.getColumnIndex("room"));
            mAlias = cursor.getString(cursor.getColumnIndex("alias"));
            mType = "section";
        } else {
            mSensor = cursor.getString(cursor.getColumnIndex("sensor"));
            mType = cursor.getString(cursor.getColumnIndex("type"));
            mUnit = cursor.getString(cursor.getColumnIndex("unit"));
            mHistory = cursor.getInt(cursor.getColumnIndex("history"));
            mIgnore = cursor.getInt(cursor.getColumnIndex("ignore"));
            mDate = cursor.getString(cursor.getColumnIndex("date"));
            mTime = cursor.getString(cursor.getColumnIndex("time"));
            mValue = "0";
        }
        mSensorsList = new ArrayList<>();
    }

    public void addSensors(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                Sensor nSensor = new Sensor(cursor, false);
                mSensorsList.add(nSensor);
            } while (cursor.moveToNext());
        }
    }

    public int getSensorsCount() {
        return mSensorsList.size();
    }

    public Sensor getSensor(int position) {
        return mSensorsList.get(position);
    }

    public String getRoom() {
        return mRoom;
    }

    public String getType() {
        return mType;
    }

    public String getSensor() {
        return mSensor;
    }

    public String getID() {
        return mID;
    }

    public String getDevice() {
        return mDevice;
    }

    public String getUnit() {
        return mUnit;
    }

    public String getValue() {
        return mValue;
    }

    public boolean isOn() {
        if (mValue.equals("on")) {
            return true;
        }
        return false;
    }

    public String getUpdate() {
        return getDate(mUpdate);
    }

    public String getTimestamp(){
        return mUpdate;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getAlias() {
        return mAlias;
    }

    public boolean getHistory() {
        return mHistory == 1 ? true : false;
    }

    public boolean getIgnore() {
        return mIgnore == 1 ? true : false;
    }

    private String getDate(String timestamp) {
        try {
            long nTimestamp = Long.parseLong(timestamp);
            Date nDate = new Date(nTimestamp * 1000);
            SimpleDateFormat nSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);
            return nSDF.format(nDate);
        } catch (Exception e) {
            Log.e("Atlantis", e.toString());
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mSensor);
        dest.writeString(mDevice);
        dest.writeString(mRoom);
        dest.writeString(mType);
        dest.writeString(mUnit);
        dest.writeString(mDate);
        dest.writeString(mTime);
        dest.writeString(mValue);
        dest.writeString(mUpdate);
        dest.writeString(mAlias);
        dest.writeInt(mHistory);
        dest.writeInt(mIgnore);
    }

    public static final Parcelable.Creator<Sensor> CREATOR = new Parcelable.Creator<Sensor>() {
        @Override
        public Sensor createFromParcel(Parcel source) {
            return new Sensor(source);
        }

        @Override
        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };

    public Sensor(Parcel in) {
        mID = in.readString();
        mSensor = in.readString();
        mDevice = in.readString();
        mRoom = in.readString();
        mType = in.readString();
        mUnit = in.readString();
        mDate = in.readString();
        mTime = in.readString();
        mValue = in.readString();
        mUpdate = in.readString();
        mAlias = in.readString();
        mHistory = in.readInt();
        mIgnore = in.readInt();
    }

    public void update(ArrayList<Sensor> sensors) {
        for(int i = 0; i < sensors.size(); i++){
            mSensorsList.get(mSensorsList.indexOf(sensors.get(i))).update(sensors.get(i));
        }
    }

    public void update(Sensor sensor){
        mValue = sensor.getValue();
        mUpdate = sensor.getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sensor) {
            return getID().equals(((Sensor) o).getID()) && getType().equals(((Sensor) o).getType());
        }
        return false;
    }
}
