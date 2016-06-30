package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.ScenarioAdapter;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Scenario;

/**
 * Created by Nawras GEORGI on 01/12/2015.
 */
public class ScenarioFragment extends ListFragment {
	private Context mContext;
	private ArrayList<Scenario> mList;
	private ScenarioAdapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_scenario, container, false);
		mContext = getActivity();
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getItems();
	}

	private void getItems(){
		ContentResolver nResolver = mContext.getContentResolver();
		Cursor nCursor = nResolver.query(AtlantisContract.Scenarios.CONTENT_URI, null, null, null, null, null);
		mList = new ArrayList<>();
		if(nCursor.moveToFirst()){
			do{
				Scenario nScenario = new Scenario(nCursor);
				mList.add(nScenario);
			}while(nCursor.moveToNext());
		}
		mAdapter = new ScenarioAdapter(mContext, mList);
		setListAdapter(mAdapter);
	}
}
