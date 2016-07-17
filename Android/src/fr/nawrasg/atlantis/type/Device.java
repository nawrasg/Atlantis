package fr.nawrasg.atlantis.type;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Device implements Parcelable{
	private String nNom, nIP, nMAC, nDNS, nPort, nType, nConnexion, nNote, mOwner;
	private int mID, nOnline; //-1 = non, 0 = NA, 1 = oui
	
	public Device(){
		nNom = nIP = nMAC = nPort = nType = nConnexion = nNote = "";
		nOnline = 0;
	}
	
	public Device(JSONObject x){
		try {
			mID = x.getInt("id");
			nNom = x.getString("nom");
			nIP = x.getString("ip");
			nMAC = x.getString("mac");
			nDNS = nPort = nNote = "";
			nType = x.getString("type");
			nConnexion = x.getString("connexion");
			nOnline = x.getInt("online");
			mOwner = x.getString("username");
		} catch (JSONException e) {
			
		}
	}

	public Device (Cursor cursor){
		mID = cursor.getInt(cursor.getColumnIndex("id"));
		nNom = cursor.getString(cursor.getColumnIndex("nom"));
		nIP = cursor.getString(cursor.getColumnIndex("ip"));
		nMAC = cursor.getString(cursor.getColumnIndex("mac"));
		nType = cursor.getString(cursor.getColumnIndex("type"));
		nNote = cursor.getString(cursor.getColumnIndex("note"));
		mOwner = cursor.getString(cursor.getColumnIndex("username"));
		nConnexion = cursor.getString(cursor.getColumnIndex("connexion"));
		nOnline = -1;
	}
	
	public String getOwner(){
		return mOwner;
	}

	public int getUser(){
		return Integer.parseInt(mOwner);
	}
	
	public int getID(){
		return mID;
	}

	public String getNom(){
		return nNom;
	}
	
	public String getIP(){
		return nIP;
	}
	
	public String getMac(){
		return nMAC;
	}
	
	public String getType(){
		return nType;
	}
	
	public String getConnexion(){
		return nConnexion;
	}
	
	public boolean isOnline(){
		return nOnline == 1;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mID);
		dest.writeString(nNom);
		dest.writeString(nIP);
		dest.writeString(nMAC);
		dest.writeString(nType);
		dest.writeString(nConnexion);
		dest.writeInt(nOnline);
		dest.writeString(mOwner);
	}
	
	public Device(Parcel in){
		mID = in.readInt();
		nNom = in.readString();
		nIP = in.readString();
		nMAC = in.readString();
		nType = in.readString();
		nConnexion = in.readString();
		nOnline = in.readInt();
		mOwner = in.readString();
	}

	public void update(Device device){
		if(device.isOnline()){
			nOnline = 1;
		}else{
			nOnline = -1;
		}
	}
	
	public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
		@Override
		public Device createFromParcel(Parcel source) {
			return new Device(source);
		}

		@Override
		public Device[] newArray(int size) {
			return new Device[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if(o instanceof Device){
			return (getID() == ((Device)o).getID());
		}
		return false;
	}
}
