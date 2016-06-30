package fr.nawrasg.atlantis.type;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Light extends PDevice implements Parcelable {
	protected String mID, mName, mProtocol, mIP, mRoom;

	public Light(JSONObject json) {
		try{
			mID = json.getString("id");
			mName = json.getString("name");
			mProtocol = json.getString("protocol");
			if(!json.isNull("ip")){
				mIP = json.getString("ip");
			}
			if(!json.isNull("room")){
				mRoom = json.getString("room");				
			}
		}catch(JSONException e){
			Log.w("Atlantis", e.toString());
		}
	}

	public Light(Cursor cursor){
		mID = cursor.getString(cursor.getColumnIndex("id"));
		mName = cursor.getString(cursor.getColumnIndex("name"));
		mProtocol = cursor.getString(cursor.getColumnIndex("protocol"));
		mIP = cursor.getString(cursor.getColumnIndex("ip"));
		mRoom = cursor.getString(cursor.getColumnIndex("room"));
	}
	
	public String getID(){
		return mID;
	}
	
	public String getName(){
		return mName;
	}
	
	public String getProtocol(){
		return mProtocol;
	}
	
	public String getIP(){
		return mIP;
	}
	
	public String getRoom(){
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
		return "light";
	}

}
