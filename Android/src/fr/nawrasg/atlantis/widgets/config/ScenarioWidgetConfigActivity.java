package fr.nawrasg.atlantis.widgets.config;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.adapters.ScenarioAdapter;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Scenario;
import fr.nawrasg.atlantis.widgets.ScenarioWidgetProvider;

/**
 * Created by Nawras on 11/07/2016.
 */

public class ScenarioWidgetConfigActivity extends ListActivity {
    private ArrayList<Scenario> mList;
    private ScenarioAdapter mAdapter;
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        Bundle nBundle = getIntent().getExtras();
        if (nBundle != null) {
            mWidgetId = nBundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        } else {
            finish();
        }

        getItems();
    }

    private void getItems() {
        ContentResolver nResolver = getContentResolver();
        Cursor nCursor = nResolver.query(AtlantisContract.Scenarios.CONTENT_URI, null, null, null, null, null);
        mList = new ArrayList<>();
        if (nCursor.moveToFirst()) {
            do {
                Scenario nScenario = new Scenario(nCursor);
                mList.add(nScenario);
            } while (nCursor.moveToNext());
        }
        mAdapter = new ScenarioAdapter(this, mList);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String nScenario = mAdapter.getItem(position).getLabel();
        App.setString(this, "Widget_Scenario_" + mWidgetId, nScenario);
        Intent nIntent = new Intent();
        nIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, nIntent);

        Intent nUpdateIntent = new Intent(this, ScenarioWidgetProvider.class);
        nUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] nWidgetIds = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ScenarioWidgetProvider.class));
        nUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, nWidgetIds);
        sendBroadcast(nUpdateIntent);

        finish();
    }
}
