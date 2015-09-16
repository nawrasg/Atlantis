package fr.nawrasg.atlantis.type;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Hue extends Light{
	private String mUID, mBrightness, mReachable, mOn;
	
	public Hue(JSONObject json) {
		super(json);
		try{
			mUID = json.getString("uid");
			mReachable = json.getString("reachable");
			mOn = json.getString("on");
			mBrightness = json.getString("brightness");
		}catch(JSONException e){

		}
	}
	
	public Hue(Parcel in){
		super(in);
		mUID = in.readString();
		mReachable = in.readString();
		mOn = in.readString();
		mBrightness = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(mUID);
		dest.writeString(mReachable);
		dest.writeString(mOn);
		dest.writeString(mBrightness);
	}
	
	public int getBrightness(){
		return Integer.parseInt(mBrightness);
	}
	
	public boolean isReachable(){
		return Boolean.parseBoolean(mReachable);
	}
	
	public boolean isOn(){
		return Boolean.parseBoolean(mOn);
	}
	
	public String getUID(){
		return mUID;
	}
	
	public static final Parcelable.Creator<Hue> CREATOR = new Parcelable.Creator<Hue>() {
		@Override
		public Hue createFromParcel(Parcel source) {
			return new Hue(source);
		}

		@Override
		public Hue[] newArray(int size) {
			return new Hue[size];
		}
	};

}
