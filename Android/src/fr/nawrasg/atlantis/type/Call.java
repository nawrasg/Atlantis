package fr.nawrasg.atlantis.type;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import fr.nawrasg.atlantis.R;


@SuppressLint("SimpleDateFormat")
public class Call {
	private String direction, number, name;
	private int length;
	private long date;
	
	public Call(JSONObject x){
		try{
			direction = x.getString("direction");
			number = x.getString("number");
			length = x.getInt("length");
			date = x.getLong("date");
			name = "Inconnu";
		}catch(Exception e){
			
		}
	}
	
	public String getNumber(){
		String nNumber = number.replace("X", "");
		nNumber = nNumber.replace(" ", "");
		return nNumber;
	}
	
	public HashMap<String, String> getMap(){
		HashMap<String, String> nMap = new HashMap<String, String>();
		nMap.put("name", name);
		if(number.equals("")){
			nMap.put("number", "Numéro Masqué");
		}else{			
			nMap.put("number", number);
		}
		if(direction.equals("outgoing")){
			nMap.put("direction", String.valueOf(R.drawable.out));
		}else if(direction.equals("incoming")){
			if(Integer.toString(length).equals("-1")){
				nMap.put("direction", String.valueOf(R.drawable.missed));
			}else{
				nMap.put("direction", String.valueOf(R.drawable.in));				
			}
		}
		nMap.put("date", getDate(date));
		nMap.put("duree", length + " secondes");
		return nMap;
	}
	
	public void setName(String x){
		name = x;
	}
	
	public void setNumber(String x){
		number = x;
	}
	
	private String getDate(long x){
		Date nDate = new Date(x*1000);
		Format nFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return nFormat.format(nDate);
	}
}
