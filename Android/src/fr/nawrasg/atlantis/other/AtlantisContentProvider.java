package fr.nawrasg.atlantis.other;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras GEORGI on 07/12/2015.
 */
public class AtlantisContentProvider extends ContentProvider {

	private static final int CALL_NOTIFIER = 0;
	private static final int EAN_LIST = 1;
	private static final int EAN_ITEM = 2;
	private static final int COURSES_LIST = 3;
	private static final int COURSES_ITEM = 4;
	private static final int SCENARIOS_LIST = 5;
	private static final int SCENARIOS_ITEM = 6;
	private static final UriMatcher URI_MATCHER;
	private AtlantisOpenHelper mHelper;

	static{
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "callnotifier", CALL_NOTIFIER);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean", EAN_LIST);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean/*", EAN_ITEM);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "courses", COURSES_LIST);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "courses/#", COURSES_ITEM);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "scenarios", SCENARIOS_LIST);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "scenarios/*", SCENARIOS_ITEM);
	}

	@Override
	public boolean onCreate() {
		mHelper = new AtlantisOpenHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase nDB = mHelper.getReadableDatabase();
		SQLiteQueryBuilder nBuilder = new SQLiteQueryBuilder();
		switch(URI_MATCHER.match(uri)){
			case CALL_NOTIFIER:
				return getCallNotifierCursor();
			case EAN_LIST:
				nBuilder.setTables("at_ean");
				break;
			case EAN_ITEM:
				nBuilder.setTables("at_ean");
				nBuilder.appendWhere("ean = " + uri.getLastPathSegment());
				break;
			case COURSES_LIST:
				nBuilder.setTables("at_courses");
				nBuilder.appendWhere("status = 0");
				break;
			case COURSES_ITEM:
				nBuilder.setTables("at_courses");
				nBuilder.appendWhere("status = 0 AND id = " + uri.getLastPathSegment());
				break;
			case SCENARIOS_LIST:
				nBuilder.setTables("at_scenarios");
				break;
		}
		Cursor nCursor = nBuilder.query(nDB, projection, selection, selectionArgs, null, null, sortOrder);
		return nCursor;
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		switch(URI_MATCHER.match(uri)){
			case EAN_LIST:
				return AtlantisContract.Ean.CONTENT_TYPE;
			case EAN_ITEM:
				return AtlantisContract.Ean.CONTENT_TYPE_ITEM;
			case SCENARIOS_LIST:
				return AtlantisContract.Scenarios.CONTENT_TYPE;
		}
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase nDB = mHelper.getWritableDatabase();
		long nID = 0;
		switch(URI_MATCHER.match(uri)){
			case EAN_LIST:
				nID = nDB.insert("at_ean", null, values);
				if(nID > 0){
					return ContentUris.withAppendedId(uri, nID);
				}
			case COURSES_LIST:
				nID = nDB.insert("at_courses", null, values);
				if(nID > 0){
					return ContentUris.withAppendedId(uri, nID);
				}
			case SCENARIOS_LIST:
				nID = nDB.insert("at_scenarios", null, values);
				if(nID > 0){
					return uri;
				}
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase nDB = mHelper.getWritableDatabase();
		int nCount = 0;
		String nID = "", nWhere = "";
		switch(URI_MATCHER.match(uri)){
			case EAN_LIST:
				return nDB.delete("at_ean", selection, selectionArgs);
			case SCENARIOS_LIST:
				return nDB.delete("at_scenarios", selection, selectionArgs);
			case SCENARIOS_ITEM:
				nID = uri.getLastPathSegment();
				nWhere = "file = " + nID;
				if(!TextUtils.isEmpty(selection)){
					nWhere += " AND " + selection;
				}
				return nDB.delete("at_scenarios", nWhere, selectionArgs);
		}
		return nCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase nDB = mHelper.getWritableDatabase();
		int nCount = 0;
		switch(URI_MATCHER.match(uri)){
			case COURSES_ITEM:
				String nID = uri.getLastPathSegment();
				String nWhere = "id = " + nID;
				if(!TextUtils.isEmpty(selection)){
					nWhere += selection;
				}
				nCount = nDB.update("at_courses", values, nWhere, selectionArgs);
				break;
		}
		return nCount;
	}

	private Cursor getCallNotifierCursor(){
		String[] columnNames = {"url", "api"};
		MatrixCursor matrixCursor = new MatrixCursor(columnNames);
		String nURL = App.getFullUrl(getContext()) + App.CALL_NOTIFIER;
		String nAPI = App.getAPI(getContext());
		matrixCursor.addRow(new String[]{nURL, nAPI});
		return matrixCursor;
	}
}
