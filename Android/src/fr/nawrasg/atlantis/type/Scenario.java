package fr.nawrasg.atlantis.type;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;
import fr.nawrasg.atlantis.interfaces.Widget;
import fr.nawrasg.atlantis.other.AtlantisContract;

/**
 * Created by Nawras GEORGI on 01/12/2015.
 */
public class Scenario implements Widget{
	private String mLabel;
	private boolean mDashboard;
	private int mOrder;

	public Scenario(JSONObject json) throws JSONException {
		mLabel = json.getString(AtlantisContract.Scenarios.COLUMN_LABEL);
	}

	public Scenario(Cursor cursor){
		mLabel = cursor.getString(cursor.getColumnIndex(AtlantisContract.Scenarios.COLUMN_LABEL));
	}

	public String getLabel(){
		return mLabel;
	}

	public boolean isDashboard(){
		return mDashboard;
	}

	@Override
	public int getOrder() {
		return mOrder;
	}

	@Override
	public void setOrder(int order) {
		mOrder = order;
	}

	@Override
	public int compareTo(Widget another) {
		if(getOrder() == another.getOrder()){
			return 0;
		}else if(getOrder() > another.getOrder()){
			return 1;
		}else{
			return -1;
		}
	}
}
