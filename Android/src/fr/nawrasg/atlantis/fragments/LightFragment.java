package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

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

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.LightAdapter;
import fr.nawrasg.atlantis.fragments.dialogs.LightDialogFragment;
import fr.nawrasg.atlantis.other.AtlantisContract;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

public class LightFragment extends ListFragment{
	private Context mContext;
	private ArrayList<Light> mList;
	private ArrayList<Room> mRoomList;
	private LightAdapter mAdapter;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_lights, container, false);
		mContext = getActivity();
		mHandler = new Handler();
		getItems();
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_light, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemLightRefresh:
				getItems();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getItems(){
		ContentResolver nResolver = mContext.getContentResolver();
		Cursor nRoomsCursor = nResolver.query(AtlantisContract.Rooms.CONTENT_URI, null, null, null, null);
		if(nRoomsCursor.moveToFirst()){
			mRoomList = new ArrayList<>();
			do{
				Room nRoom = new Room(nRoomsCursor);
				mRoomList.add(nRoom);
			}while(nRoomsCursor.moveToNext());
		}
		Cursor nLightsCursor = nResolver.query(AtlantisContract.Lights.CONTENT_URI, null, null, null, null);
		if(nLightsCursor.moveToFirst()){
			mList = new ArrayList<>();
			do{
				Light nLight = new Hue(nLightsCursor);
				mList.add(nLight);
			}while(nLightsCursor.moveToNext());
			mAdapter = new LightAdapter(mContext, mList, mRoomList);
			setListAdapter(mAdapter);
		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setItemListener();
		getStatus();
	}

	private void getStatus() {
		String nURL = App.getFullUrl(mContext) + App.LIGHTS + "?api=" + App.getAPI(mContext);
		Request nRequest = new Request.Builder()
				.url(nURL)
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				try {
					JSONObject nJson = new JSONObject(response.body().string());
					JSONArray arr = nJson.getJSONArray("lights");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject json = arr.getJSONObject(i);
						Light nLight = new Hue(json);
						int nI = mList.indexOf(nLight);
						((Hue)mList.get(nI)).update((Hue)nLight);
					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mAdapter.notifyDataSetChanged();
						}
					});
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}

	private void toggleLight(Light light) {
		String nURL = App.getFullUrl(mContext) + App.LIGHTS + "?api=" + App.getAPI(mContext) + "&toggle=" + ((Hue)light).getUID();
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

			}
		});
	}

	private void setItemListener() {
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleLight(mList.get(position));
			}
		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Light nLight = mList.get(position);
				LightDialogFragment nLightDialog = new LightDialogFragment();
				Bundle nArgs;
				nArgs = setArgs(nLight);
				nLightDialog.setArguments(nArgs);
				nLightDialog.show(getFragmentManager(), "light");
				return true;
			}
		});
	}

	private Bundle setArgs(Light light) {
		Bundle nBundle = new Bundle();
		nBundle.putParcelableArrayList("rooms", mRoomList);
		nBundle.putParcelable("light", light);
		return nBundle;
	}
}
