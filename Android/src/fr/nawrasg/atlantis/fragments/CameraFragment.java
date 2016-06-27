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

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.CameraAdapter;
import fr.nawrasg.atlantis.type.Camera;

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
		String nURL = App.getFullUrl(mContext) + App.CAMERAS + "?api=" + App.getAPI(mContext);
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				if(response.code() == 202){
					mList = new ArrayList<Camera>();
					try {
						JSONArray nArr = new JSONArray(response.body().string());
						for (int i = 0; i < nArr.length(); i++) {
							Camera nCamera = new Camera(nArr.getJSONObject(i));
							mList.add(nCamera);
						}
						mRecyclerView.setAdapter(new CameraAdapter(mContext, mList));
					} catch (JSONException e) {
						Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}
}
