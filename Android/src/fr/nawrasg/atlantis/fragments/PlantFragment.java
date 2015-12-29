package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.PlantAdapter;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.type.Plant;

public class PlantFragment extends Fragment {
	private Context mContext;
	private ArrayList<Plant> mList;
	private RecyclerView mRecyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_plant, container, false);
		getActivity().getActionBar().setIcon(R.drawable.ng_plant);
		mContext = super.getActivity();
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
		new PlantGET(mContext).execute(App.PLANTE, "get");
	}
	
	

	private class PlantGET extends DataGET {

		public PlantGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				mList = new ArrayList<Plant>();
				try {
					JSONArray nArr = new JSONArray(result);
					for (int i = 0; i < nArr.length(); i++) {
						Plant nPlant = new Plant(nArr.getJSONObject(i));
						mList.add(nPlant);
					}
					mRecyclerView.setAdapter(new PlantAdapter(mContext, mList));
				} catch (JSONException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}

	}
}