package fr.nawrasg.atlantis.type;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.interfaces.Widget;

public class Light extends PDevice implements Parcelable, Widget{
    protected String mID, mName, mProtocol, mIP, mRoom;
    protected int mOrder;

    public static final String HUE = "hue";
    public static final String TYPE = "light";

    public Light(JSONObject json) {
        try {
            mID = json.getString(AtlantisContract.Lights.COLUMN_ID);
            mName = json.getString(AtlantisContract.Lights.COLUMN_LABEL);
            mProtocol = json.getString(AtlantisContract.Lights.COLUMN_PROTOCOL);
            if (!json.isNull(AtlantisContract.Lights.COLUMN_IP)) {
                mIP = json.getString(AtlantisContract.Lights.COLUMN_IP);
            }
            if (!json.isNull(AtlantisContract.Lights.COLUMN_ROOM)) {
                mRoom = json.getString(AtlantisContract.Lights.COLUMN_ROOM);
            }
        } catch (JSONException e) {
            Log.w("Atlantis", e.toString());
        }
    }

    public Light(Cursor cursor) {
        mID = cursor.getString(cursor.getColumnIndex(AtlantisContract.Lights.COLUMN_ID));
        mName = cursor.getString(cursor.getColumnIndex(AtlantisContract.Lights.COLUMN_LABEL));
        mProtocol = cursor.getString(cursor.getColumnIndex(AtlantisContract.Lights.COLUMN_PROTOCOL));
        mIP = cursor.getString(cursor.getColumnIndex(AtlantisContract.Lights.COLUMN_IP));
        mRoom = cursor.getString(cursor.getColumnIndex(AtlantisContract.Lights.COLUMN_ROOM));
    }

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getProtocol() {
        return mProtocol;
    }

    public String getIP() {
        return mIP;
    }

    public String getRoom() {
        return mRoom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mName);
        dest.writeString(mProtocol);
        dest.writeString(mIP);
        dest.writeString(mRoom);
    }

    public static final Parcelable.Creator<Light> CREATOR = new Parcelable.Creator<Light>() {
        @Override
        public Light createFromParcel(Parcel source) {
            return new Light(source);
        }

        @Override
        public Light[] newArray(int size) {
            return new Light[size];
        }
    };

    public Light(Parcel in) {
        mID = in.readString();
        mName = in.readString();
        mProtocol = in.readString();
        mIP = in.readString();
        mRoom = in.readString();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Light) {
            return (getID().equals(((Light) o).getID()));
        }
        return false;
    }

    @Override
    public int getOrder() {
        return mOrder;
    }

    @Override
    public void setOrder(int order) {
        mOrder = order;
    }

    @Override
    public int compareTo(Widget another) {
        if(getOrder() == another.getOrder()){
            return 0;
        }else if(getOrder() > another.getOrder()){
            return 1;
        }else{
            return -1;
        }
    }
}
