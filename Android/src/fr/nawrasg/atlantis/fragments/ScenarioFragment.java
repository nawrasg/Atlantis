package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.ScenarioAdapter;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras on 31/10/2016.
 */

public class ScenarioFragment extends Fragment {
    private Context mContext;
    private ArrayList<Scenario> mList;
    private ScenarioAdapter mAdapter;
    @Bind(R.id.rvScenario)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nView = inflater.inflate(R.layout.fragment_scenario, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, nView);
        setHasOptionsMenu(true);
        return nView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        getItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_scenarios, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemScenarioDashboard:
                mAdapter.modeDashboard();
                return true;
        }
        return false;
    }

    private void getItems() {
        ContentResolver nResolver = mContext.getContentResolver();
        Cursor nCursor = nResolver.query(AtlantisContract.Scenarios.CONTENT_URI, null, null, null, null, null);
        mList = new ArrayList<>();
        if (nCursor.moveToFirst()) {
            do {
                Scenario nScenario = new Scenario(nCursor);
                mList.add(nScenario);
            } while (nCursor.moveToNext());
        }
        mAdapter = new ScenarioAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
