package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
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
import fr.nawrasg.atlantis.adapters.PharmacieAdapter;
import fr.nawrasg.atlantis.type.Medicament;

public class PharmacieFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipePharmacie)
    SwipeRefreshLayout mSwipeLayout;
    private Context mContext;
    private ArrayList<Medicament> nList;
    private PharmacieAdapter mAdapter;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mHandler = new Handler();
        View nV = inflater.inflate(R.layout.fragment_pharmacie, container, false);
        ButterKnife.bind(this, nV);
        if (getActivity().findViewById(R.id.main_fragment2) == null) {
            setHasOptionsMenu(true);
        }
        mSwipeLayout.setOnRefreshListener(this);
        return nV;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setProgressBar(true);
        getItems();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_pharmacie, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPharmacieAdd:
                ((MainActivity) getActivity()).loadFragment(new PharmacieAddFragment(), true);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_pharmacie, menu);
    }

    public void getItems() {
        String nURL = App.getUri(mContext, App.PHARMACIE);
        Request nRequest = new Request.Builder().url(nURL).build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    nList = new ArrayList<Medicament>();
                    try {
                        JSONArray arr = new JSONArray(response.body().string());
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jdata = arr.getJSONObject(i);
                            Medicament nMed = new Medicament(jdata);
                            nList.add(nMed);
                        }
                        mAdapter = new PharmacieAdapter(mContext, nList);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setListAdapter(mAdapter);
                                mSwipeLayout.setRefreshing(false);
                                ((MainActivity) getActivity()).setProgressBar(false);
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("Atlantis", e.toString());
                    }
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Medicament nMedicament = nList.get(position);
        switch (item.getItemId()) {
            case R.id.itemPharmaciePlus:
                modifyItem(nMedicament, '+');
                return true;
            case R.id.itemPharmacieMinus:
                modifyItem(nMedicament, '-');
                return true;
            case R.id.itemPharmacieDel:
                deleteItem(nMedicament);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void modifyItem(final Medicament medicament, final char mode) {
        String nURL = App.getUri(mContext, App.PHARMACIE);
        switch (mode) {
            case '+':
                nURL += "&id=" + medicament.getID() + "&qte=" + (medicament.getQuantity() + 1);
                break;
            case '-':
                nURL += "&id=" + medicament.getID() + "&qte=" + (medicament.getQuantity() - 1);
                break;
        }
        Request nRequest = new Request.Builder()
                .url(nURL)
                .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                switch (mode) {
                    case '+':
                        medicament.increment();
                        break;
                    case '-':
                        medicament.decrement();
                        break;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void deleteItem(final Medicament medicament) {
        String nURL = App.getUri(mContext, App.PHARMACIE) + "&id=" + medicament.getID();
        Request nRequest = new Request.Builder()
                .url(nURL)
                .delete()
                .build();
        App.httpClient.newCall(nRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 202) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.remove(medicament);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getItems();
    }
}