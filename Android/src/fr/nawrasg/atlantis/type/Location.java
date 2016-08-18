package fr.nawrasg.atlantis.type;

import android.content.Context;
import android.location.LocationManager;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras on 18/08/2016.
 */

public class Location {
    public static final String HOME_LATITUDE = "homeLat";
    public static final String HOME_LONGITUDE = "homeLong";
    public static final String HOME_RADIUS = "homeRadius";
    public static final String POSITIONING_PROVIDER = "gps";

    private Context mContext;

    public Location(Context context){
        mContext = context;
    }

    public void setHomeCoordinates(float latitude, float longitude){
        App.setFloat(mContext, HOME_LATITUDE, latitude);
        App.setFloat(mContext, HOME_LONGITUDE, longitude);
    }

    public void setHomeRadius(float radius){
        App.setFloat(mContext, HOME_RADIUS, radius);
    }

    public boolean isGPSProvider(){
        return App.getPrefBoolean(mContext, POSITIONING_PROVIDER);
    }

    public float getHomeLatitude(){
        return App.getFloat(mContext, HOME_LATITUDE);
    }

    public float getHomeLongitude(){
        return App.getFloat(mContext, HOME_LONGITUDE);
    }

    public float getHomeRadius(){
        return App.getFloat(mContext, HOME_RADIUS);
    }

    public String getProvider(){
        if(isGPSProvider()){
            return LocationManager.GPS_PROVIDER;
        }
        return LocationManager.NETWORK_PROVIDER;
    }
}
