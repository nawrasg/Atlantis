package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import fr.nawrasg.atlantis.adapters.KitchenAdapter;
import fr.nawrasg.atlantis.type.Produit;

/**
 * Created by Nawras on 16/11/2016.
 */

public class KitchenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private Handler mHandler;
    private KitchenAdapter mAdapter;

    @Bind(R.id.rvKitchen)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeKitchen)
    SwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_kitchen, container, false);
        ButterKnife.bind(this, nView);
        mContext = getActivity();
        mHandler = new Handler();
        mSwipeLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        return nView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.courses_layout), StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.courses_layout), StaggeredGridLayoutManager.VERTICAL));
        ((MainActivity) getActivity()).setProgressBar(true);
        get();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_kitchen, menu);
        SearchView nSV = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.itemSearch));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null)
                    mAdapter.getFilter().filter(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        nSV.setOnQueryTextListener(queryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCuisineAdd:
                ((MainActivity) getActivity()).loadFragment(new CuisineAddFragment(), true);
                return true;
        }
        return false;
    }

    public void get() {
        String nURL = App.getUri(mContext, App.CUISINE);
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
                    ArrayList<Produit> nList = new ArrayList<>();
                    try {
                        JSONArray nArr = new JSONArray(response.body().string());
                        for (int i = 0; i < nArr.length(); i++) {
                            JSONObject nJson = nArr.getJSONObject(i);
                            Produit nProduit = new Produit(nJson);
                            nList.add(nProduit);
                        }
                        mAdapter = new KitchenAdapter(nList);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(mRecyclerView.getAdapter() == null){
                                    mRecyclerView.setAdapter(mAdapter);
                                }else{
                                    mRecyclerView.swapAdapter(mAdapter, false);
                                }
                                mSwipeLayout.setRefreshing(false);
                                ((MainActivity) getActivity()).setProgressBar(false);
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("Atlantis", e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        get();
    }
}
