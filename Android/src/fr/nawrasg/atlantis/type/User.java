package fr.nawrasg.atlantis.type;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	private int mID, mType;
	private String mName, mAPI;
	
	public User(){
		mID = -99;
		mName = "";
		mType = -99;
		mAPI = "";
	}
	
	public User(JSONObject nJson){
		try{
			mID = nJson.getInt("id");
			mType = nJson.getInt("type");
			mName = nJson.getString("nom");
			mAPI = nJson.optString("cle");
		}catch(JSONException e){
			
		}
	}
	
	public User(Parcel user){
		mID = user.readInt();
		mName = user.readString();
		mType = user.readInt();
		mAPI = user.readString();
	}
	
	public String getName(){
		return mName;
	}
	
	public String getAPI(){
		return mAPI;
	}
	
	public int getID(){
		return mID;
	}
	
	public int getType(){
		return mType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mID);
		dest.writeString(mName);
		dest.writeInt(mType);
		dest.writeString(mAPI);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	
	public boolean equals(Object o) {
		if(o != null && o instanceof User){
			return this.getName().equals(((User)o).getName());
		}
		return false;
	}
}
