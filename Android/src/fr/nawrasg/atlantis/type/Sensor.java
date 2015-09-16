package fr.nawrasg.atlantis.type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import fr.nawrasg.atlantis.R;

public class Sensor extends PDevice implements Parcelable {
	private String mID, mSensor, mDevice, mRoom, mType, mUnit, mDate, mTime, mValue, mUpdate, mAlias;
	private int mHistory, mIgnore;

	public Sensor(JSONObject json) {
		try {
			mDevice = json.optString("device");
			mType = json.optString("type");
			if (!mType.equals("section")) {
				mID = json.optString("id");
				mSensor = json.optString("sensor");
				mUnit = json.optString("unit");
				mDate = json.optString("date");
				mTime = json.optString("time");
				mValue = json.optString("value");
				mUpdate = json.optString("update");
				mHistory = json.optInt("history");
				mIgnore = json.optInt("ignore");
			} else {
				mRoom = json.optString("room");
				mAlias = json.optString("alias");
			}
		} catch (Exception e) {

		}
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
}
