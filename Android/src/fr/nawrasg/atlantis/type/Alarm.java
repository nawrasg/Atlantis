package fr.nawrasg.atlantis.type;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.nawrasg.atlantis.App;

/**
 * Created by Nawras on 20/08/2016.
 */

public class Alarm {
    public static final String MODE_DAY = "day";
    public static final String MODE_NIGHT = "night";
    public static final String MODE_AWAY = "away";

    private Context mContext;
    private Handler mHandler;

    public Alarm(Context context){
        mContext = context;
        mHandler = null;
    }

    public Alarm(Context context, Handler handler){
        mContext = context;
        mHandler = handler;
    }

    public void setMode(String mode){
        String nURL = App.getUri(mContext, App.HOME) + "&mode=" + mode;
        FormEncodingBuilder nBody = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(nURL)
                .put(nBody.build())
                .build();

        App.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //TODO
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if(response.code() == 202){
                    JSONObject nResponse = null;
                    try {
                        nResponse = new JSONObject(response.body().string());
                        String nMode = nResponse.getString("mode");
                        if(mHandler != null){
                            Message nMessage = new Message();
                            Bundle nBundle = new Bundle();
                            nBundle.putString("mode", nMode);
                            nMessage.setData(nBundle);
                            mHandler.sendMessage(nMessage);
                        }
                    } catch (JSONException e) {
                        //TODO
                    }
                }
            }
        });
    }
}
