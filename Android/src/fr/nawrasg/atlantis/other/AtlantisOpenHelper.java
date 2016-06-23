package fr.nawrasg.atlantis.other;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.nawrasg.atlantis.interfaces.AtlantisDatabaseInterface;

/**
 * Created by Nawras GEORGI on 25/03/2016.
 */
public class AtlantisOpenHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "atlantis";
	public static final int DATABASE_VERSION = 1;

	public AtlantisOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AtlantisDatabaseInterface.EAN_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
