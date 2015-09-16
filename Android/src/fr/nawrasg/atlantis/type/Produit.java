package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

public class Produit {
	private String nTitre, mEAN;
	private int nQuantite, mID;
	private long nDate, nDate2;
	private String nPlace;
	private int nStatus, mIgnore;

	public Produit(JSONObject x) {
		try {
			mID = x.getInt("id");
			nTitre = x.getString("label");
			mEAN = x.getString("ean");
			nQuantite = x.getInt("quantite");
			nDate = x.getLong("peremption");
			nPlace = x.getString("endroit");
			nStatus = x.getInt("status");
			nDate2 = x.getLong("ouvert");
			mIgnore = x.getInt("ignore");
		} catch (Exception e) {

		}
	}

	public int getID(){
		return mID;
	}

	public boolean isIgnore(){
		return mIgnore == 0 ? false : true;
	}

	public String getEAN(){
		return mEAN;
	}

	public int getQuantite() {
		return nQuantite;
	}

	public String getTitre() {
		return nTitre;
	}

	public String getPlace() {
		return nPlace;
	}

	public long getDate() {
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
	
	public long getDate2() {
		if (Math.abs(nDate2) > 365) {
			return nDate2/365;
		}else if(Math.abs(nDate2) > 30){
			return nDate2/30;
		}
		return nDate2;
	}
	
	public String getDate2Unit() {
		if (Math.abs(nDate2) > 365) {
			return "ans";
		}else if(Math.abs(nDate2) > 30){
			return "mois";
		}
		return "jours";
	}
	
	public int getStatus(){
		return nStatus;
	}

	public void increment(){
		nQuantite++;
	}

	public void decrement(){
		nQuantite--;
	}

	public void ignore(){
		mIgnore = 1;
	}

	public void open(){
		nStatus = 1;
		nDate2 = 0;
	}
}
