package fr.nawrasg.atlantis.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.squareup.okhttp.OkHttpClient;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Alarm;

/**
 * Created by Nawras on 17/07/2016.
 */

public class AlarmWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int nWidgetId = appWidgetIds[i];

            Intent nDayIntent = new Intent(context, AlarmWidgetProvider.class);
            nDayIntent.setAction(Alarm.MODE_DAY);
            PendingIntent nDayPendingIntent = PendingIntent.getBroadcast(context, 0, nDayIntent, 0);

            Intent nNightIntent = new Intent(context, AlarmWidgetProvider.class);
            nNightIntent.setAction(Alarm.MODE_NIGHT);
            PendingIntent nNightPendingIntent = PendingIntent.getBroadcast(context, 0, nNightIntent, 0);

            Intent nAwayIntent = new Intent(context, AlarmWidgetProvider.class);
            nAwayIntent.setAction(Alarm.MODE_AWAY);
            PendingIntent nAwayPendingIntent = PendingIntent.getBroadcast(context, 0, nAwayIntent, 0);

            RemoteViews nViews = new RemoteViews(context.getPackageName(), R.layout.widget_alarm);
            nViews.setOnClickPendingIntent(R.id.btnWidgetAlarmDay, nDayPendingIntent);
            nViews.setOnClickPendingIntent(R.id.btnWidgetAlarmNight, nNightPendingIntent);
            nViews.setOnClickPendingIntent(R.id.btnWidgetAlarmAway, nAwayPendingIntent);
            appWidgetManager.updateAppWidget(nWidgetId, nViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (App.httpClient == null) {
            App.httpClient = new OkHttpClient();
        }
        switch (intent.getAction()) {
            case Alarm.MODE_DAY:
                (new Alarm(context)).setMode(Alarm.MODE_DAY);
                break;
            case Alarm.MODE_NIGHT:
                (new Alarm(context)).setMode(Alarm.MODE_NIGHT);
                break;
            case Alarm.MODE_AWAY:
                (new Alarm(context)).setMode(Alarm.MODE_AWAY);
                break;
        }
    }


}
