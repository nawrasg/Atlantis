package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

public class GPS {
	private String mUser, mMAC, mDate;
	private double mLat, mLong;
	
	public GPS(JSONObject json){
		try{
			//mUser = json.getString("username");
			mMAC = json.getString("mac");
			mLat = json.getDouble("lat");
			mLong = json.getDouble("long");
			mDate = json.getString("timestamp");
		}catch(Exception e){
			
		}
	}
	
	/*public String getUser(){
		return mUser;
	}*/
	
	public String getMAC(){
		return mMAC;
	}
	
	public double getLatitude(){
		return mLat;
	}
	
	public double getLongitude(){
		return mLong;
	}
	
	public String getTimestamp(){
		return mDate;
	}
}
