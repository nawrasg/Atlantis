package fr.nawrasg.atlantis.type;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nawras GEORGI on 01/12/2015.
 */
public class Scenario {
	private String mLabel;

	public Scenario(JSONObject json) throws JSONException {
		mLabel = json.getString("file");
	}

	public String getLabel(){
		return mLabel;
	}
}
