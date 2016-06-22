package fr.nawrasg.atlantis.other;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nawras GEORGI on 25/03/2016.
 */
public class AtlantisSQLite extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "atlantis";
	public static final int DATABASE_VERSION = 1;

	public AtlantisSQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String nCourses = "CREATE TABLE";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
