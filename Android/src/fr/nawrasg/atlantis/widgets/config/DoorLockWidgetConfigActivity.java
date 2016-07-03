package fr.nawrasg.atlantis.widgets.config;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.widgets.DoorLockWidgetProvider;

/**
 * Created by Nawras on 03/07/2016.
 */

public class DoorLockWidgetConfigActivity extends Activity {
    private int mWidgetId;
    @Bind(R.id.txtConfigWidgetDoorLockName)
    EditText txtLockName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_door_lock);
        ButterKnife.bind(this);
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
    }

    @OnClick(R.id.btnConfigWidgetDoorLockDone)
    public void save() {
        String nLockName = txtLockName.getText().toString();
        App.setString(this, "Widget_DoorLock_" + mWidgetId, nLockName);
        Intent nIntent = new Intent();
        nIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, nIntent);

        Intent nUpdateIntent = new Intent(this, DoorLockWidgetProvider.class);
        nUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] nWidgetIds = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), DoorLockWidgetProvider.class));
        nUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, nWidgetIds);
        sendBroadcast(nUpdateIntent);

        finish();
    }
}
