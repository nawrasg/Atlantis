package fr.nawrasg.atlantis.other;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
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
    private OkHttpClient mClient;

    public AtlantisSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mResolver = context.getContentResolver();
        mClient = new OkHttpClient();
    }

    public AtlantisSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mResolver = context.getContentResolver();
        mClient = new OkHttpClient();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String nURL = App.getFullUrl(mContext) + App.EAN + "?api=" + App.getAPI(mContext);
        final Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        mClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 202){
                    try {
                        JSONArray arr = new JSONArray(response.body().string());
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject nJson = arr.getJSONObject(i);
                            Uri nUri = Uri.parse("content://fr.nawrasg.atlantis/ean");
                            ContentValues nValues = new ContentValues();
                            nValues.put("ean", nJson.getString("ean"));
                            nValues.put("nom", nJson.getString("nom"));
                            mResolver.insert(nUri, nValues);
                        }
                    } catch (JSONException e) {
                        Log.w("Atlantis", e.toString());
                    }
                }
            }
        });
    }
}
