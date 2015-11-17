package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import fr.nawrasg.atlantis.adapters.CameraAdapter;
import fr.nawrasg.atlantis.adapters.PlantAdapter;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.type.Camera;
import fr.nawrasg.atlantis.type.Plant;

/**
 * Created by Nawras GEORGI on 17/11/2015.
 */
public class CameraFragment extends Fragment {
	private Context mContext;
	private RecyclerView mRecyclerView;
	private ArrayList<Camera> mList;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_camera, container, false);
		mContext = getActivity();
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rvCamera);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		getItems();
	}

	private void getItems(){
		new CamerasGET(mContext).execute(App.CAMERAS);
	}

	private class CamerasGET extends DataGET{

		public CamerasGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				mList = new ArrayList<Camera>();
				try {
					JSONArray nArr = new JSONArray(result);
					for (int i = 0; i < nArr.length(); i++) {
						Camera nCamera = new Camera(nArr.getJSONObject(i));
						mList.add(nCamera);
					}
					mRecyclerView.setAdapter(new CameraAdapter(mContext, mList));
				} catch (JSONException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}
	}
}
