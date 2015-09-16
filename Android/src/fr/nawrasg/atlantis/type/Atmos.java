package fr.nawrasg.atlantis.type;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Atmos {
	private String nTemp, nHum, nDate;
	
	public Atmos(JSONObject x){
		try {
			nTemp = x.getString("temp");
			nHum = x.getString("hum");
			nDate = x.getString("date");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getMap(){
		HashMap<String, String> nMap = new HashMap<String, String>();
		nMap.put("temp", nTemp);
		nMap.put("hum", nHum);
		nMap.put("date", nDate);
		return nMap;
	}
	
	public String getTemp(){
		return nTemp;
	}
	
	public String getHum(){
		return nHum;
	}
	
	public String getDate(){
		return nDate;
	}
}
