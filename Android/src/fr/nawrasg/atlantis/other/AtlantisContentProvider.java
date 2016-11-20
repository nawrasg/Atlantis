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
import fr.nawrasg.atlantis.interfaces.AtlantisDatabaseInterface;

/**
 * Created by Nawras GEORGI on 07/12/2015.
 */
public class AtlantisContentProvider extends ContentProvider {

    private static final int CONNECTION = 0;
    private static final int EAN_LIST = 1;
    private static final int EAN_ITEM = 2;
    private static final int COURSES_LIST = 3;
    private static final int COURSES_ITEM = 4;
    private static final int SCENARIOS_LIST = 5;
    private static final int SCENARIOS_ITEM = 6;
    private static final int LIGHTS_LIST = 7;
    private static final int LIGHTS_ITEM = 8;
    private static final int ROOMS_LIST = 9;
    private static final int ROOMS_ITEM = 10;
    private static final int PLANTS_LIST = 11;
    private static final int PLANTS_ITEM = 12;
    private static final int DEVICES_LIST = 13;
    private static final int DEVICES_ITEM = 14;
    private static final int SENSORS_LIST = 15;
    private static final int SENSORS_ITEM = 16;
    private static final int SENSORS_DEVICES_LIST = 17;
    private static final int SENSORS_DEVICES_ITEM = 18;
    private static final int WIDGETS_LIST = 19;
    private static final int WIDGETS_ITEM = 20;
    private static final UriMatcher URI_MATCHER;
    private AtlantisOpenHelper mHelper;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "connection", CONNECTION);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean", EAN_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "ean/*", EAN_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "courses", COURSES_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "courses/#", COURSES_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "scenarios", SCENARIOS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "scenarios/*", SCENARIOS_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "lights", LIGHTS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "lights/*", LIGHTS_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "rooms", ROOMS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "rooms/*", ROOMS_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "plants", PLANTS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "plants/*", PLANTS_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "devices", DEVICES_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "devices/*", DEVICES_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "sensors", SENSORS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "sensors/*", SENSORS_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "sensors_devices", SENSORS_DEVICES_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "sensors_devices/*", SENSORS_DEVICES_ITEM);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "widgets", WIDGETS_LIST);
        URI_MATCHER.addURI(AtlantisContract.AUTHORITY, "widgets/*", WIDGETS_ITEM);
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
        switch (URI_MATCHER.match(uri)) {
            case CONNECTION:
                return getConnection();
            case EAN_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.EAN_TABLE_NAME);
                break;
            case EAN_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.EAN_TABLE_NAME);
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
                nBuilder.setTables(AtlantisDatabaseInterface.SCENARIOS_TABLE_NAME);
                break;
            case LIGHTS_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.LIGHTS_TABLE_NAME);
                break;
            case LIGHTS_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.LIGHTS_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case ROOMS_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.ROOMS_TABLE_NAME);
                break;
            case ROOMS_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.ROOMS_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case PLANTS_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.PLANTS_TABLE_NAME);
                break;
            case PLANTS_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.PLANTS_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case DEVICES_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.DEVICES_TABLE_NAME);
                break;
            case DEVICES_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.DEVICES_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case SENSORS_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.SENSORS_TABLE_NAME);
                break;
            case SENSORS_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.SENSORS_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case SENSORS_DEVICES_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.SENSORS_DEVICES_TABLE_NAME);
                break;
            case SENSORS_DEVICES_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.SENSORS_DEVICES_TABLE_NAME);
                nBuilder.appendWhere("id = " + uri.getLastPathSegment());
                break;
            case WIDGETS_LIST:
                nBuilder.setTables(AtlantisDatabaseInterface.TABLE_NAME_WIDGETS);
                break;
            case WIDGETS_ITEM:
                nBuilder.setTables(AtlantisDatabaseInterface.TABLE_NAME_WIDGETS);
                nBuilder.appendWhere("_id = " + uri.getLastPathSegment());
                break;
        }
        Cursor nCursor = nBuilder.query(nDB, projection, selection, selectionArgs, null, null, sortOrder);
        return nCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case EAN_LIST:
                return AtlantisContract.Ean.CONTENT_TYPE;
            case EAN_ITEM:
                return AtlantisContract.Ean.CONTENT_TYPE_ITEM;
            case SCENARIOS_LIST:
                return AtlantisContract.Scenarios.CONTENT_TYPE;
            case LIGHTS_LIST:
                return AtlantisContract.Lights.CONTENT_TYPE;
            case LIGHTS_ITEM:
                return AtlantisContract.Lights.CONTENT_TYPE_ITEM;
            case ROOMS_LIST:
                return AtlantisContract.Rooms.CONTENT_TYPE;
            case ROOMS_ITEM:
                return AtlantisContract.Rooms.CONTENT_TYPE_ITEM;
            case PLANTS_LIST:
                return AtlantisContract.Plants.CONTENT_TYPE;
            case PLANTS_ITEM:
                return AtlantisContract.Plants.CONTENT_TYPE_ITEM;
            case DEVICES_LIST:
                return AtlantisContract.Devices.CONTENT_TYPE;
            case DEVICES_ITEM:
                return AtlantisContract.Devices.CONTENT_TYPE_ITEM;
            case SENSORS_LIST:
                return AtlantisContract.Sensors.CONTENT_TYPE;
            case SENSORS_ITEM:
                return AtlantisContract.Sensors.CONTENT_TYPE_ITEM;
            case SENSORS_DEVICES_LIST:
                return AtlantisContract.SensorsDevices.CONTENT_TYPE;
            case SENSORS_DEVICES_ITEM:
                return AtlantisContract.SensorsDevices.CONTENT_TYPE_ITEM;
            case WIDGETS_LIST:
                return AtlantisContract.Widgets.CONTENT_TYPE;
            case WIDGETS_ITEM:
                return AtlantisContract.Widgets.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase nDB = mHelper.getWritableDatabase();
        long nID = 0;
        switch (URI_MATCHER.match(uri)) {
            case EAN_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.EAN_TABLE_NAME, null, values);
                if (nID > 0) {
                    return ContentUris.withAppendedId(uri, nID);
                }
            case COURSES_LIST:
                nID = nDB.insert("at_courses", null, values);
                if (nID > 0) {
                    return ContentUris.withAppendedId(uri, nID);
                }
            case SCENARIOS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.SCENARIOS_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case LIGHTS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.LIGHTS_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case ROOMS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.ROOMS_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case PLANTS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.PLANTS_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case DEVICES_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.DEVICES_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case SENSORS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.SENSORS_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case SENSORS_DEVICES_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.SENSORS_DEVICES_TABLE_NAME, null, values);
                if (nID > 0) {
                    return uri;
                }
            case WIDGETS_LIST:
                nID = nDB.insert(AtlantisDatabaseInterface.TABLE_NAME_WIDGETS, null, values);
                if (nID > 0) {
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
        switch (URI_MATCHER.match(uri)) {
            case EAN_LIST:
                return nDB.delete(AtlantisDatabaseInterface.EAN_TABLE_NAME, selection, selectionArgs);
            case SCENARIOS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.SCENARIOS_TABLE_NAME, selection, selectionArgs);
            case SCENARIOS_ITEM:
                nID = uri.getLastPathSegment();
                nWhere = "file = " + nID;
                if (!TextUtils.isEmpty(selection)) {
                    nWhere += " AND " + selection;
                }
                return nDB.delete(AtlantisDatabaseInterface.SCENARIOS_TABLE_NAME, nWhere, selectionArgs);
            case LIGHTS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.LIGHTS_TABLE_NAME, selection, selectionArgs);
            case ROOMS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.ROOMS_TABLE_NAME, selection, selectionArgs);
            case PLANTS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.PLANTS_TABLE_NAME, selection, selectionArgs);
            case DEVICES_LIST:
                return nDB.delete(AtlantisDatabaseInterface.DEVICES_TABLE_NAME, selection, selectionArgs);
            case SENSORS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.SENSORS_TABLE_NAME, selection, selectionArgs);
            case SENSORS_DEVICES_LIST:
                return nDB.delete(AtlantisDatabaseInterface.SENSORS_DEVICES_TABLE_NAME, selection, selectionArgs);
            case WIDGETS_LIST:
                return nDB.delete(AtlantisDatabaseInterface.TABLE_NAME_WIDGETS, selection, selectionArgs);
        }
        return nCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase nDB = mHelper.getWritableDatabase();
        int nCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case COURSES_ITEM:
                String nID = uri.getLastPathSegment();
                String nWhere = "id = " + nID;
                if (!TextUtils.isEmpty(selection)) {
                    nWhere += selection;
                }
                nCount = nDB.update("at_courses", values, nWhere, selectionArgs);
                break;
        }
        return nCount;
    }

    private Cursor getCallNotifierCursor() {
        String[] columnNames = {"url", "api"};
        MatrixCursor matrixCursor = new MatrixCursor(columnNames);
        String nURL = App.getFullUrl(getContext()) + App.CALL_NOTIFIER;
        String nAPI = App.getAPI(getContext());
        matrixCursor.addRow(new String[]{nURL, nAPI});
        return matrixCursor;
    }

    private Cursor getConnection() {
        String[] columnNames = {"url", "api"};
        MatrixCursor matrixCursor = new MatrixCursor(columnNames);
        String nURL = App.getFullUrl(getContext());
        String nAPI = App.getAPI(getContext());
        matrixCursor.addRow(new String[]{nURL, nAPI});
        return matrixCursor;
    }
}
