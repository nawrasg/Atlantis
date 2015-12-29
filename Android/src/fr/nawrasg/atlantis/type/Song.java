package fr.nawrasg.atlantis.type;

import org.json.JSONObject;

public class Song {
	private String nTitle, nPath;
	private String nLength, nType;
	private int nID;

	public Song(JSONObject song) {
		try {
			nType = song.getString("type");
			nTitle = song.getString("title");
			nID = song.getInt("id");
			if (nType.equals("song")) {
				nPath = song.getString("path");
				nLength = song.getString("length");
			} else {
				nPath = "";
				nLength = "";
			}
		} catch (Exception e) {

		}
	}

	public String getTitle() {
		return nTitle;
	}

	public String getPath() {
		return nPath;
	}

	public String getLength() {
		return nLength;
	}

	public String getType() {
		return nType;
	}

	public int getID() {
		return nID;
	}

}
