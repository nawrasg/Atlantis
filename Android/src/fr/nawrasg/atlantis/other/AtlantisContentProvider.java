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

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras GEORGI on 07/12/2015.
 */
public class AtlantisContentProvider extends ContentProvider {

	private static final int EAN_LIST = 1;
	private static final int EAN_ITEM = 2;
	private static final UriMatcher URI_MATCHER;
	private AtlantisOpenHelper mHelper;

	static{
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean", EAN_LIST);
		URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean/*", EAN_ITEM);
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
			case EAN_LIST:
				nBuilder.setTables("at_ean");
				break;
			case EAN_ITEM:
				nBuilder.setTables("at_ean");
				nBuilder.appendWhere("ean = " + uri.getLastPathSegment());
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
		}
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase nDB = mHelper.getWritableDatabase();
		switch(URI_MATCHER.match(uri)){
			case EAN_LIST:
			case EAN_ITEM:
				long nID = nDB.insert("at_ean", null, values);
				if(nID > 0){
					return ContentUris.withAppendedId(uri, nID);
				}
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
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
