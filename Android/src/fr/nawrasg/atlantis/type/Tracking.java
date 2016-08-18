package fr.nawrasg.atlantis.type;

import android.util.Log;

import org.json.JSONObject;

public class Tracking {
    private String mUser, mMAC, mDate;
    private double mLat, mLong;

    public Tracking(JSONObject json) {
        try {
            mUser = json.getString("nom");
            mMAC = json.getString("mac");
            mLat = json.getDouble("lat");
            mLong = json.getDouble("long");
            mDate = json.getString("timestamp");
        } catch (Exception e) {
            Log.e("Atlantis", e.toString());
        }
    }

    public String getUser() {
        return mUser;
    }

    public String getMAC() {
        return mMAC;
    }

    public double getLatitude() {
        return mLat;
    }

    public double getLongitude() {
        return mLong;
    }

    public String getTimestamp() {
        return mDate;
    }
}
