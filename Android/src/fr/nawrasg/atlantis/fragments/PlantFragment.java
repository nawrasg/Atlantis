package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.PlantAdapter;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Plant;

public class PlantFragment extends Fragment {
    private Context mContext;
    private ArrayList<Plant> mList;
    @Bind(R.id.rvPlant)
    RecyclerView mRecyclerView;
    private Handler mHandler;
    private PlantAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_plant, container, false);
        ButterKnife.bind(this, nView);
        getActivity().getActionBar().setIcon(R.drawable.ng_plant);
        mContext = super.getActivity();
        mHandler = new Handler();
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        getItems();
        getStatus();
    }

    private void getItems() {
        mList = new ArrayList<Plant>();
        ContentResolver nResolver = mContext.getContentResolver();
        Cursor nCursor = nResolver.query(AtlantisContract.Plants.CONTENT_URI, null, null, null, null);
        if (nCursor.moveToFirst()) {
            do {
                Plant nPlant = new Plant(nCursor);
                mList.add(nPlant);
            } while (nCursor.moveToNext());
        }
        mAdapter = new PlantAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getStatus() {
        String nURL = App.getFullUrl(mContext) + App.PLANTE + "?api=" + App.getAPI(mContext);
        Request nRequest = new Request.Builder().url(nURL).build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    try {
                        JSONArray nArr = new JSONArray(response.body().string());
                        for (int i = 0; i < nArr.length(); i++) {
                            Plant nPlant = new Plant(nArr.getJSONObject(i));
                            int nIndex = mList.indexOf(nPlant);
                            mList.get(nIndex).update(nPlant);
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("Atlantis", e.toString());
                    }
                }
            }
        });
    }
}