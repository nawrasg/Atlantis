package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import fr.nawrasg.atlantis.adapters.LightAdapter;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

/**
 * Created by Nawras on 29/10/2016.
 */

public class LightFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private Handler mHandler;
    private ArrayList<Light> mList;
    private ArrayList<Room> mRoomList;
    @Bind(R.id.rvLight)
    RecyclerView mRecyclerView;
    private LightAdapter mAdapter;
    @Bind(R.id.swipeLight)
    SwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_light, container, false);
        ButterKnife.bind(this, nView);
        mContext = getActivity();
        mHandler = new Handler();
        mSwipeLayout.setOnRefreshListener(this);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ((MainActivity) getActivity()).setProgressBar(true);
        getItems();
        getStatus();
    }

    private void getItems() {
        mList = new ArrayList<Light>();
        ContentResolver nResolver = mContext.getContentResolver();
        Cursor nRoomsCursor = nResolver.query(AtlantisContract.Rooms.CONTENT_URI, null, null, null, null);
        if (nRoomsCursor.moveToFirst()) {
            mRoomList = new ArrayList<>();
            do {
                Room nRoom = new Room(nRoomsCursor);
                mRoomList.add(nRoom);
            } while (nRoomsCursor.moveToNext());
        }
        Cursor nCursor = nResolver.query(AtlantisContract.Lights.CONTENT_URI, null, null, null, null);
        if (nCursor.moveToFirst()) {
            do {
                Light nLight = new Hue(nCursor);
                mList.add(nLight);
            } while (nCursor.moveToNext());
        }
        mAdapter = new LightAdapter(mContext, mList, mRoomList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getStatus() {
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
                            mSwipeLayout.setRefreshing(false);
                            ((MainActivity) getActivity()).setProgressBar(false);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Atlantis", e.toString());
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getStatus();
    }
}
