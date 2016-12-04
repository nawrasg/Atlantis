package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.adapters.WidgetAdapter;
import fr.nawrasg.atlantis.interfaces.AtlantisDatabaseInterface;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.other.AtlantisOpenHelper;
import fr.nawrasg.atlantis.type.Alarm;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Scenario;
import fr.nawrasg.atlantis.type.Widget;

/**
 * Created by Nawras on 29/10/2016.
 */

public class WidgetsFragment extends Fragment {
    private Context mContext;
    private FloatingActionButton mFAB;
    private FloatingActionMenu mActionMenu;
    private SubActionButton mDayButton, mNightButton, mAwayButton;
    private Handler mHandler;
    private Alarm mAlarm;
    private ArrayList<Object> mList;
    private WidgetAdapter mAdapter;

    @Bind(R.id.rvWidget)
    RecyclerView mRecyclerView;
    @Bind(R.id.imgWidgetWeatherToday)
    ImageView mWeatherIcon;
    @Bind(R.id.imgWidgetWeatherTomorrow)
    ImageView mWeather2Icon;
    @Bind(R.id.imgWidgetAlarm)
    ImageView mAlarmIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_widgets, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, nView);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setModeLabel(msg.getData().getString("mode"));
            }
        };
        mAlarm = new Alarm(mContext, mHandler);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.fragment_widgets_column), StaggeredGridLayoutManager.VERTICAL));
        createFAB();
        ((MainActivity) getActivity()).setProgressBar(true);
        get();
        //getItems();
        //getLightStatus();
        loadWidgets();
        ((MainActivity) getActivity()).setProgressBar(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.fragment_widgets_column), StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onDestroy() {
        if (mFAB != null) {
            mFAB.detach();
        }
        super.onDestroy();
    }

    private void get() {
        String nURL = App.getUri(mContext, App.HOME);
        Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //TODO
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    try {
                        JSONObject nJson = new JSONObject(response.body().string());
                        JSONArray nArr = nJson.getJSONArray("weather");
                        final JSONObject nJsonW1 = nArr.getJSONObject(0);
                        final JSONObject nJsonW2 = nArr.getJSONObject(1);
                        final String nMode = nJson.getString("mode");
                        if (isAdded()) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setWeatherToday(nJsonW1);
                                    setWeatherTomorrow(nJsonW2);
                                    setModeLabel(nMode);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        //TODO
                    }

                }
            }
        });
    }

    private void getLightStatus() {
        String nURL = App.getUri(mContext, App.LIGHTS);
        Request nRequest = new Request.Builder()
                .url(nURL)
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject nJson = new JSONObject(response.body().string());
                    JSONArray arr = nJson.getJSONArray("lights");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject json = arr.getJSONObject(i);
                        Light nLight = new Hue(json);
                        int nI = mList.indexOf(nLight);
                        if (nI > -1) {
                            ((Hue) mList.get(nI)).update((Hue) nLight);
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            //mSwipeLayout.setRefreshing(false);
                            ((MainActivity) getActivity()).setProgressBar(false);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }

    private void getItems() {
        ContentResolver nResolver = mContext.getContentResolver();
        mList = new ArrayList<>();
        Cursor nLightCursor = nResolver.query(AtlantisContract.Lights.CONTENT_URI, null, null, null, null);
        if (nLightCursor.moveToFirst()) {
            do {
                Light nLight = new Hue(nLightCursor);
                mList.add(nLight);
            } while (nLightCursor.moveToNext());
        }

    }

    private void loadWidgets() {
        AtlantisOpenHelper nHelper = new AtlantisOpenHelper(mContext);
        SQLiteDatabase nDB = nHelper.getReadableDatabase();
        mList = new ArrayList<>();
        getScenarios(nDB);
        mAdapter = new WidgetAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getScenarios(SQLiteDatabase db) {
        String nQuery = "SELECT * FROM " + AtlantisDatabaseInterface.SCENARIOS_TABLE_NAME + " INNER JOIN " + AtlantisDatabaseInterface.TABLE_NAME_WIDGETS + " ON " +
                AtlantisContract.Scenarios.COLUMN_LABEL + " = " + AtlantisContract.Widgets.COLUMN_ITEM + " WHERE " + AtlantisContract.Widgets.COLUMN_TYPE + " = " + Widget.WIDGET_SCENARIO;
        Cursor nCursor = db.rawQuery(nQuery, null);
        if (nCursor != null && nCursor.moveToFirst()) {
            do {
                Scenario nScenario = new Scenario(nCursor);
                mList.add(nScenario);
            } while (nCursor.moveToNext());
        }
    }

    private Drawable getDraw(int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(resource, mContext.getTheme());
        } else {
            return getResources().getDrawable(resource, null);
        }
    }

    private void createFAB() {
        ImageView nMenuIcon = new ImageView(getActivity());
        Drawable nMenuDrawable = getDraw(R.drawable.ic_dehaze_white_24dp);
        nMenuIcon.setImageDrawable(nMenuDrawable);
        mFAB = new FloatingActionButton.Builder(getActivity())
                .setContentView(nMenuIcon)
                .setTheme(FloatingActionButton.THEME_DARK)
                .build();

        ImageView nDayIcon = new ImageView(getActivity());
        Drawable nDayDrawable = getDraw(R.drawable.ic_weekend_white_18dp);
        nDayIcon.setImageDrawable(nDayDrawable);
        SubActionButton.Builder nItemBuilder = new SubActionButton.Builder(getActivity());
        mDayButton = nItemBuilder
                .setContentView(nDayIcon)
                .setTheme(SubActionButton.THEME_DARK)
                .build();
        mDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarm.setMode(Alarm.MODE_DAY);
                mActionMenu.close(true);
            }
        });

        ImageView nNightIcon = new ImageView(getActivity());
        Drawable nNightDrawable = getDraw(R.drawable.ic_airline_seat_individual_suite_white_18dp);
        nNightIcon.setImageDrawable(nNightDrawable);
        mNightButton = nItemBuilder
                .setContentView(nNightIcon)
                .setTheme(SubActionButton.THEME_DARK)
                .build();
        mNightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarm.setMode(Alarm.MODE_NIGHT);
                mActionMenu.close(true);
            }
        });

        ImageView nAwayIcon = new ImageView(getActivity());
        Drawable nAwayDrawable = getDraw(R.drawable.ic_directions_walk_white_18dp);
        nAwayIcon.setImageDrawable(nAwayDrawable);
        mAwayButton = nItemBuilder
                .setContentView(nAwayIcon)
                .setTheme(SubActionButton.THEME_DARK)
                .build();
        mAwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarm.setMode(Alarm.MODE_AWAY);
                mActionMenu.close(true);
            }
        });

        mActionMenu = new FloatingActionMenu
                .Builder(getActivity())
                .addSubActionView(mDayButton)
                .addSubActionView(mNightButton)
                .addSubActionView(mAwayButton)
                .attachTo(mFAB)
                .build();
    }

    private void setModeLabel(String mode) {
        String nModePrefix = getResources().getString(R.string.fragment_home_mode);
        String nModePost = "";
        int nModeIcon = 0;
        switch (mode) {
            case "day":
                nModePost = getResources().getString(R.string.fragment_home_mode_day);
                nModeIcon = R.drawable.ic_lock_open_white_36dp;
                break;
            case "night":
                nModePost = getResources().getString(R.string.fragment_home_mode_night);
                nModeIcon = R.drawable.ic_lock_outline_white_36dp;
                break;
            case "away":
                nModePost = getResources().getString(R.string.fragment_home_mode_away);
                nModeIcon = R.drawable.ic_lock_white_36dp;
                break;
        }
        //txtMode.setText(nModePrefix + " " + nModePost);
        if (nModeIcon != 0) {
            mAlarmIcon.setImageResource(nModeIcon);
        }
    }

    private void setWeatherToday(JSONObject json) {
        String nCode = json.optString("code");
        double nTemp = json.optDouble("temperature");
        String nDescription = json.optString("description");
        int nWeatherIcon = 0;
        switch (nCode) {
            case "01d":
                nWeatherIcon = R.drawable.ng_01d_96px;
                break;
            case "01n":
                nWeatherIcon = R.drawable.ng_01n_96px;
                break;
            case "02d":
                nWeatherIcon = R.drawable.ng_02d_96px;
                break;
            case "02n":
                nWeatherIcon = R.drawable.ng_02n_96px;
                break;
            case "03d":
            case "03n":
                nWeatherIcon = R.drawable.ng_03_96px;
                break;
            case "04d":
            case "04n":
                nWeatherIcon = R.drawable.ng_04_96px;
                break;
            case "09d":
            case "09n":
                nWeatherIcon = R.drawable.ng_09_96px;
                break;
            case "10d":
            case "10n":
                nWeatherIcon = R.drawable.ng_10_96px;
                break;
            case "11d":
            case "11n":
                nWeatherIcon = R.drawable.ng_11_96px;
                break;
            case "13d":
            case "13n":
                nWeatherIcon = R.drawable.ng_13_96px;
                break;
            case "50d":
            case "50n":
                nWeatherIcon = R.drawable.ng_50_96px;
                break;
            default:
                nWeatherIcon = R.drawable.ng_na_96px;
                break;
        }
        mWeatherIcon.setImageResource(nWeatherIcon);
    }

    private void setWeatherTomorrow(JSONObject json) {
        String nCode = json.optString("code");
        double nTemp = json.optDouble("temperature");
        String nDescription = json.optString("description");
        int nWeatherIcon = 0;
        switch (nCode) {
            case "01d":
                nWeatherIcon = R.drawable.ng_01d_36px;
                break;
            case "01n":
                nWeatherIcon = R.drawable.ng_01n_36px;
                break;
            case "02d":
                nWeatherIcon = R.drawable.ng_02d_36px;
                break;
            case "02n":
                nWeatherIcon = R.drawable.ng_02n_36px;
                break;
            case "03d":
            case "03n":
                nWeatherIcon = R.drawable.ng_03_36px;
                break;
            case "04d":
            case "04n":
                nWeatherIcon = R.drawable.ng_04_36px;
                break;
            case "09d":
            case "09n":
                nWeatherIcon = R.drawable.ng_09_36px;
                break;
            case "10d":
            case "10n":
                nWeatherIcon = R.drawable.ng_10_36px;
                break;
            case "11d":
            case "11n":
                nWeatherIcon = R.drawable.ng_11_36px;
                break;
            case "13d":
            case "13n":
                nWeatherIcon = R.drawable.ng_13_36px;
                break;
            case "50d":
            case "50n":
                nWeatherIcon = R.drawable.ng_50_36px;
                break;
            default:
                nWeatherIcon = R.drawable.ng_na_36px;
                break;
        }
        mWeather2Icon.setImageResource(nWeatherIcon);
    }
}
