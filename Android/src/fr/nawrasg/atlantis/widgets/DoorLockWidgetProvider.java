package fr.nawrasg.atlantis.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;

/**
 * Created by Nawras on 02/07/2016.
 */

public class DoorLockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int nWidgetId = appWidgetIds[i];

            String nLockUri = "nuki://name/" + App.getString(context, "Widget_DoorLock_" + nWidgetId);
            Intent nIntent = new Intent();
            nIntent.setData(Uri.parse(nLockUri));
            PendingIntent nPendingIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

            RemoteViews nViews = new RemoteViews(context.getPackageName(), R.layout.widget_door_lock);
            nViews.setOnClickPendingIntent(R.id.btnWidgetDoorLockAction, nPendingIntent);

            appWidgetManager.updateAppWidget(nWidgetId, nViews);
        }
    }
}
