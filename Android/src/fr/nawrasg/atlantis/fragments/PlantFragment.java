package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
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
import fr.nawrasg.atlantis.type.Plant;

public class PlantFragment extends Fragment {
	private Context mContext;
	private ArrayList<Plant> mList;
	@Bind(R.id.rvPlant)
	private RecyclerView mRecyclerView;
	private Handler mHandler;

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
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPlant);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		getItems();
	}

	private void getItems() {
		mList = new ArrayList<Plant>();
		String nURL = App.getFullUrl(mContext) + App.PLANTE + "?api=" + App.getAPI(mContext) + "&get";
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				if(response.code() == 202){
					mList = new ArrayList<Plant>();
					try {
						JSONArray nArr = new JSONArray(response.body().string());
						for (int i = 0; i < nArr.length(); i++) {
							Plant nPlant = new Plant(nArr.getJSONObject(i));
							mList.add(nPlant);
						}
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								mRecyclerView.setAdapter(new PlantAdapter(mContext, mList));
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