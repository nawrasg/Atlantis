package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

public class AtmosMM {
	private String sMinTemp, sMaxTemp, sMinHum, sMaxHum, sDate;
	
	public AtmosMM(JSONObject x){
		try{
			sDate = x.getString("date");
			sMinTemp = x.getString("minTemp");
			sMaxTemp = x.getString("maxTemp");
			sMinHum = x.getString("minHum");
			sMaxHum = x.getString("maxHum");
		}catch(Exception e){
			
		}
	}
	
	public String getMinTemp(){
		return sMinTemp;
	}
	
	public String getMaxTemp(){
		return sMaxTemp;
	}
	
	public String getMinHum(){
		return sMinHum;
	}
	
	public String getMaxHum(){
		return sMaxHum;
	}
	
	public String getDate(){
		return sDate;
	}
}
