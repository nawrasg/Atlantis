package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

public class Entretien {
	private String nTitre;
	private int nQuantite, mID;
	private long nDate;
	
	public Entretien(JSONObject x){
		try{
			mID = x.getInt("id");
			nTitre = x.getString("nom");
			nQuantite = x.getInt("quantite");
			nDate = x.getLong("peremption");
		}catch(Exception e){
			
		}
	}

	public int getID(){
		return mID;
	}
	
	public String getTitle(){
		return nTitre;
	}
	
	public int getQuantity(){
		return nQuantite;
	}
	
	public long getDate(){
		if (Math.abs(nDate) > 365) {
			return nDate/365;
		}else if(Math.abs(nDate) > 30){
			return nDate/30;
		}
		return nDate;
	}
	
	public String getDateUnit() {
		if (Math.abs(nDate) > 365) {
			return "ans";
		}else if(Math.abs(nDate) > 30){
			return "mois";
		}
		return "jours";
	}

	public void increment(){
		nQuantite++;
	}

	public void decrement(){
		nQuantite--;
	}
}
