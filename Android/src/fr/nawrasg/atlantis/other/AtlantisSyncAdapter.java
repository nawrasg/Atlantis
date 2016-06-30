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
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 202){
                    try {
                        JSONObject nJSON = new JSONObject(response.body().string());
                        if(!nJSON.isNull("scenarios")){
                            insertScenarios(nJSON.getJSONArray("scenarios"));
                        }
                        App.setLong(mContext, "lastmodified", (System.currentTimeMillis()/1000));
                    } catch (JSONException e) {
                        Log.w("Atlantis", e.toString());
                    }
                }
            }
        });
    }

    private void insertScenarios(JSONArray nArr) throws JSONException {
        mResolver.delete(AtlantisContract.Scenarios.CONTENT_URI, null, null);
        for(int i = 0; i < nArr.length(); i++){
            JSONObject nJson = nArr.getJSONObject(i);
            ContentValues nValues = new ContentValues();
            nValues.put("file", nJson.getString("file"));
            mResolver.insert(AtlantisContract.Scenarios.CONTENT_URI, nValues);
        }
    }
}
