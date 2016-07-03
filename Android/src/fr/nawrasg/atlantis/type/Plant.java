package fr.nawrasg.atlantis.type;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;


public class Plant extends PDevice implements Parcelable{
	private int mID, mRoom, mBattery;
	private String mSensor, mTitle, mPicture, mColor, mDate, mTime;
	private double mLight, mConductivity, mSoilTemperature, mAirTemperature, mMoisture;
	
	public Plant (JSONObject json){
		try {
			mID = json.optInt("id");
			mSensor = json.optString("sensor");
			mTitle = json.optString("title");
			mPicture = json.optString("picture");
			mColor = json.optString("color");
			mRoom = json.optInt("room");
			mBattery = json.optInt("battery");
			mLight = json.optDouble("light");
			mConductivity = json.optDouble("conductivity");
			mSoilTemperature = json.optDouble("stemperature");
			mAirTemperature = json.optDouble("atemperature");
			mMoisture = json.optDouble("moisture");
			mDate = json.optString("date");
			mTime = json.getString("time");
		} catch (JSONException e) {
			
		}
	}

	public Plant(Cursor cursor){
		mID = cursor.getInt(cursor.getColumnIndex("id"));
		mSensor = cursor.getString(cursor.getColumnIndex("sensor"));
		mTitle = cursor.getString(cursor.getColumnIndex("title"));
		mPicture = cursor.getString(cursor.getColumnIndex("picture"));
		mColor = cursor.getString(cursor.getColumnIndex("color"));
		mRoom = cursor.getInt(cursor.getColumnIndex("room"));
	}
	
	public Plant(Parcel source) {
		mID = source.readInt();
		mSensor = source.readString();
		mTitle = source.readString();
		mPicture = source.readString();
		mColor = source.readString();
		mRoom = source.readInt();
		mBattery = source.readInt();
		mLight = source.readDouble();
		mConductivity = source.readDouble();
		mSoilTemperature = source.readDouble();
		mAirTemperature = source.readDouble();
		mMoisture = source.readDouble();
		mDate = source.readString();
		mTime = source.readString();
	}

	public void update(Plant plant){
		mBattery = plant.getBatteryLevel();
		mLight = plant.getLight();
		mConductivity = plant.getConductivity();
		mMoisture = plant.getMoisture();
		mAirTemperature = plant.getAirTemperature();
		mSoilTemperature = plant.getSoilTemperature();
		mDate = plant.getDate();
		mTime = plant.getTime();
	}

	public double getConductivity(){
		return mConductivity;
	}
	
	public double getSoilTemperature(){
		return mSoilTemperature;
	}
	
	public double getAirTemperature(){
		return mAirTemperature;
	}
	
	public double getMoisture(){
		return mMoisture;
	}
	
	public Bitmap getPicture(){
		byte[] nDecodedByte = Base64.decode(mPicture, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(nDecodedByte, 0, nDecodedByte.length);
	}
	
	public int getId(){
		return mID;
	}
	
	public int getRoom(){
		return mRoom;
	}
	
	public int getBatteryLevel(){
		return mBattery;
	}
	
	public String getSensor(){
		return mSensor;
	}
	
	public String getTitle(){
		return mTitle;
	}
	
	public String getColor(){
		return mColor;
	}
	
	public String getDate(){
		return mDate;
	}
	
	public String getTime(){
		return mTime;
	}
	
	public double getLight(){
		return mLight;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mID);
		dest.writeString(mSensor);
		dest.writeString(mTitle);
		dest.writeString(mPicture);
		dest.writeString(mColor);
		dest.writeInt(mRoom);
		dest.writeInt(mBattery);
		dest.writeDouble(mLight);
		dest.writeDouble(mConductivity);
		dest.writeDouble(mSoilTemperature);
		dest.writeDouble(mAirTemperature);
		dest.writeDouble(mMoisture);
		dest.writeString(mDate);
		dest.writeString(mTime);
	}
	
	public static final Parcelable.Creator<Plant> CREATOR = new Parcelable.Creator<Plant>() {
		@Override
		public Plant createFromParcel(Parcel source) {
			return new Plant(source);
		}

		@Override
		public Plant[] newArray(int size) {
			return new Plant[size];
		}
	};


	@Override
	public String getID() {
		return mID + "";
	}

	@Override
	public String getType() {
		return "plant";
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Plant){
			return (getID().equals(((Plant)o).getID()));
		}
		return false;
	}
}
