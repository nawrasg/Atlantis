package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.ScenarioAdapter;
import fr.nawrasg.atlantis.async.DataGET;
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
		new ScenarioGET(mContext).execute(App.SCENARIOS);
	}

	private class ScenarioGET extends DataGET{

		public ScenarioGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mList = new ArrayList<>();
			try {
				JSONArray nArr = new JSONArray(result);
				for(int i = 0; i < nArr.length(); i++){
					Scenario nScenario = new Scenario(nArr.getJSONObject(i));
					mList.add(nScenario);
				}
				mAdapter = new ScenarioAdapter(mContext, mList);
				setListAdapter(mAdapter);
			} catch (JSONException e) {
				Log.e("Atlantis", e.toString());
			}
		}
	}

}
