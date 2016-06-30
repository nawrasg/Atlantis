package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import fr.nawrasg.atlantis.App;
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
	private Handler mHandler;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_scenario, container, false);
		mContext = getActivity();
		mHandler = new Handler();
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

	private void getItemsLegacy(){
		String nURL = App.getFullUrl(mContext) + App.SCENARIOS + "?api=" + App.getAPI(mContext);
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				mList = new ArrayList<>();
				try {
					JSONArray nArr = new JSONArray(response.body().string());
					for(int i = 0; i < nArr.length(); i++){
						Scenario nScenario = new Scenario(nArr.getJSONObject(i));
						mList.add(nScenario);
					}
					mAdapter = new ScenarioAdapter(mContext, mList);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							setListAdapter(mAdapter);
						}
					});
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}
}
