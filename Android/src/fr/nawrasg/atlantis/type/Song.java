package fr.nawrasg.atlantis.type;

import java.util.HashMap;

import org.json.JSONObject;

import fr.nawrasg.atlantis.R;

public class Song {
	private String nTitle, nPath;
	private String nLength, nType;
	private int nID;

	public Song(JSONObject song) {
		try {
			nType = song.getString("type");
			nTitle = song.getString("title");
			nID = song.getInt("id");
			if(nType.equals("song")){
				nPath = song.getString("path");
				nLength = song.getString("length");				
			}else{
				nPath = "";
				nLength = "";
			}
		} catch (Exception e) {

		}
	}

	public String getTitle() {
		return nTitle;
	}

	public String getPath(){
		return nPath;
	}
	
	public String getLength() {
		return nLength;
	}
	
	public HashMap<String, String> getMap(){
		HashMap<String, String> nMap = new HashMap<String, String>();
		nMap.put("title", nTitle);
		nMap.put("length", nLength);
		if(nType.equals("playlist")){
			nMap.put("icon", String.valueOf(R.drawable.playlist));
		}else{
			nMap.put("icon", String.valueOf(R.drawable.mp3));
		}
		return nMap;
	}
	
	public String getType(){
		return nType;
	}
	
	public int getID(){
		return nID;
	}

}
