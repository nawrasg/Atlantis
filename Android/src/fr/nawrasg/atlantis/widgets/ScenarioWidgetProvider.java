package fr.nawrasg.atlantis.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;

/**
 * Created by Nawras on 11/07/2016.
 */

public class ScenarioWidgetProvider extends AppWidgetProvider {
    public static final String SCENARIO_PLAY = "fr.nawrasg.atlantis.widgets.scenario.PLAY";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int i = 0; i < appWidgetIds.length; i++){
            int nWidgetId = appWidgetIds[i];

            String nScenario = App.getString(context, "Widget_Scenario_" + nWidgetId);
            Intent nIntent = new Intent(context, ScenarioWidgetProvider.class);
            nIntent.setAction(SCENARIO_PLAY);
            nIntent.putExtra("scenario", nScenario);
            PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, 0, nIntent, 0);

            RemoteViews nViews = new RemoteViews(context.getPackageName(), R.layout.widget_scenario);
            nViews.setTextViewText(R.id.btnWidgetScenarioPlay, nScenario);
            nViews.setOnClickPendingIntent(R.id.btnWidgetScenarioPlay, nPendingIntent);

            appWidgetManager.updateAppWidget(nWidgetId, nViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(App.httpClient == null){
            App.httpClient = new OkHttpClient();
        }
        switch(intent.getAction()){
            case SCENARIO_PLAY:
                String nScenario = intent.getStringExtra("scenario");
                try {
                    playScenario(context, nScenario);
                } catch (UnsupportedEncodingException e) {
                    Log.e("Atlantis", e.toString());
                }
                break;
        }
        super.onReceive(context, intent);
    }

    private void playScenario(Context context, String scenario) throws UnsupportedEncodingException {
        String nLabel = URLEncoder.encode(scenario, "UTF-8");
        String nURL = App.getUri(context, App.SCENARIOS) + "&scenario=" + nLabel;
        Request nRequest = new Request.Builder().url(nURL).build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }
}
