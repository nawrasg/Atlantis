package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

/**
 * Created by Nawras GEORGI on 17/11/2015.
 */
public class Camera {

	public Camera(JSONObject camera){
		mID = camera.optInt("id");
		mRoom = camera.optInt("room");
		mIP = camera.optString("ip");
		mType = camera.optString("type");
		mImageUrl = camera.optString("image");
		mVideoUrl = camera.optString("video");
		mUsername = camera.optString("username");
		mPassword = camera.optString("password");
		mAlias = camera.optString("alias");
	}

	public int getID() {
		return mID;
	}

	public int getRoom() {
		return mRoom;
	}

	public String getIP() {
		return mIP;
	}

	public String getType() {
		return mType;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public String getVideoUrl() {
		return mVideoUrl;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getAlias() {
		return mAlias;
	}

	private int mID, mRoom;
	private String mIP, mType, mImageUrl, mVideoUrl, mUsername, mPassword, mAlias;

}
