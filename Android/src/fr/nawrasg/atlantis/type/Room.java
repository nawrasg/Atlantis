package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
	private String mRoom, mID;

	public Room(JSONObject json) {
		try {
			mID = json.getString("id");
			mRoom = json.getString("room");
		} catch (Exception e) {

		}
	}
	
	public String getID() {
		return mID;
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
		dest.writeString(mRoom);
	}

	public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
		@Override
		public Room createFromParcel(Parcel source) {
			return new Room(source);
		}

		@Override
		public Room[] newArray(int size) {
			return new Room[size];
		}
	};
	
	public Room(Parcel in) {
		mID = in.readString();
		mRoom = in.readString();
	}

}
