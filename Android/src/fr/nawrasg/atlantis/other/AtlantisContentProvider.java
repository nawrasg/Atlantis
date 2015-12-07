package fr.nawrasg.atlantis.other;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras GEORGI on 07/12/2015.
 */
public class AtlantisContentProvider extends ContentProvider {
	@Override
	public boolean onCreate() {
		return false;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		switch(selection){
			case "call_notifier":
				return getCallNotifierCursor();
			default:
				return null;
		}
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
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
