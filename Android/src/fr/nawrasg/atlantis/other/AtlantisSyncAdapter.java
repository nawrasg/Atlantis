package fr.nawrasg.atlantis.other;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras on 24/06/2016.
 */

public class AtlantisSyncAdapter extends AbstractThreadedSyncAdapter {
    private Context mContext;
    private ContentResolver mResolver;

    public AtlantisSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mResolver = context.getContentResolver();
    }

    public AtlantisSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String nURL = App.getFullUrl(mContext) + App.SYNC + "?api=" + App.getAPI(mContext) + "&lastmodified=-1";
        final Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        if (App.httpClient == null) {
            App.httpClient = new OkHttpClient();
        }
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    try {
                        JSONObject nJSON = new JSONObject(response.body().string());
                        if (!nJSON.isNull("scenarios")) {
                            insertScenarios(nJSON.getJSONArray("scenarios"));
                        }
                        if (!nJSON.isNull("ean")) {
                            insertEan(nJSON.getJSONArray("ean"));
                        }
                        if (!nJSON.isNull("lights")) {
                            insertLights(nJSON.getJSONArray("lights"));
                        }
                        if (!nJSON.isNull("rooms")) {
                            insertRooms(nJSON.getJSONArray("rooms"));
                        }
                        if (!nJSON.isNull("plants")) {
                            insertPlants(nJSON.getJSONArray("plants"));
                        }
                        if (!nJSON.isNull("devices")) {
                            insertDevices(nJSON.getJSONArray("devices"));
                        }
                        if (!nJSON.isNull("sensors")) {
                            insertSensors(nJSON.getJSONObject("sensors"));
                        }
                        if(!nJSON.isNull("user")){
                            getUser(nJSON.getJSONObject("user"));
                        }
                        App.setLong(mContext, "lastmodified", (System.currentTimeMillis() / 1000));
                    } catch (JSONException e) {
                        Log.w("Atlantis", e.toString());
                    }
                }
            }
        });
    }

    private void insertScenarios(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Scenarios.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("file", nJson.getString("file"));
            mResolver.insert(AtlantisContract.Scenarios.CONTENT_URI, nValues);
        }
    }

    private void insertEan(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Ean.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("ean", nJson.getString("ean"));
            nValues.put("nom", nJson.getString("nom"));
            mResolver.insert(AtlantisContract.Ean.CONTENT_URI, nValues);
        }
    }

    private void insertLights(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Lights.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("name", nJson.getString("name"));
            nValues.put("protocol", nJson.getString("protocol"));
            nValues.put("type", nJson.getString("type"));
            nValues.put("ip", nJson.getString("ip"));
            nValues.put("room", nJson.getInt("room"));
            nValues.put("uid", nJson.getString("uid"));
            mResolver.insert(AtlantisContract.Lights.CONTENT_URI, nValues);
        }
    }

    private void insertRooms(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Rooms.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("room", nJson.getString("room"));
            mResolver.insert(AtlantisContract.Rooms.CONTENT_URI, nValues);
        }
    }

    private void insertPlants(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Plants.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("sensor", nJson.getString("sensor"));
            nValues.put("title", nJson.getString("title"));
            nValues.put("picture", nJson.getString("picture"));
            nValues.put("color", nJson.getString("color"));
            nValues.put("room", nJson.getInt("room"));
            nValues.put("timestamp", nJson.getString("timestamp"));
            mResolver.insert(AtlantisContract.Plants.CONTENT_URI, nValues);
        }
    }

    private void insertDevices(JSONArray array) throws JSONException {
        mResolver.delete(AtlantisContract.Devices.CONTENT_URI, null, null);
        for (int i = 0; i < array.length(); i++) {
            JSONObject nJson = array.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("nom", nJson.getString("nom"));
            nValues.put("ip", nJson.getString("ip"));
            nValues.put("mac", nJson.getString("mac"));
            nValues.put("dns", nJson.getString("dns"));
            nValues.put("port", -1);
            nValues.put("type", nJson.getString("type"));
            nValues.put("connexion", nJson.getString("connexion"));
            nValues.put("note", nJson.getString("note"));
            nValues.put("username", nJson.getString("username"));
            mResolver.insert(AtlantisContract.Devices.CONTENT_URI, nValues);
        }
    }

    private void insertSensors(JSONObject json) throws JSONException {
        JSONArray nDevicesArray = json.getJSONArray("devices");
        mResolver.delete(AtlantisContract.SensorsDevices.CONTENT_URI, null, null);
        for (int i = 0; i < nDevicesArray.length(); i++) {
            JSONObject nJson = nDevicesArray.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("device", nJson.getString("device"));
            nValues.put("alias", nJson.getString("alias"));
            nValues.put("room", nJson.getInt("room"));
            mResolver.insert(AtlantisContract.SensorsDevices.CONTENT_URI, nValues);
        }
        JSONArray nSensorsArray = json.getJSONArray("sensors");
        mResolver.delete(AtlantisContract.Sensors.CONTENT_URI, null, null);
        for (int i = 0; i < nSensorsArray.length(); i++) {
            JSONObject nJson = nSensorsArray.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("id", nJson.getInt("id"));
            nValues.put("sensor", nJson.getString("sensor"));
            nValues.put("device", nJson.getString("device"));
            nValues.put("protocol", nJson.getString("protocol"));
            nValues.put("type", nJson.getString("type"));
            nValues.put("unit", nJson.getString("unit"));
            nValues.put("history", nJson.getInt("history"));
            nValues.put("date", nJson.getString("date"));
            nValues.put("time", nJson.getString("time"));
            nValues.put("ignore", nJson.getInt("ignore"));
            mResolver.insert(AtlantisContract.Sensors.CONTENT_URI, nValues);
        }
    }

    private void getUser(JSONObject json) throws JSONException {
        String nName = json.getString("nom");
        int nType = json.getInt("type");
        App.setString(mContext, "username", nName);
        App.setInt(mContext, "usertype", nType);
    }
}
