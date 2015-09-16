package fr.nawrasg.atlantis.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.LightAdapter;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.fragments.dialogs.LightDialogFragment;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

public class LightFragment extends ListFragment{
	private Context mContext;
	private ArrayList<Light> mList;
	private ArrayList<Room> mRoomList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_lights, container, false);
		mContext = getActivity();
		getItems();
		setHasOptionsMenu(true);
		return nView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getItems();
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

	private void getItems() {
		new LightsGET(mContext).execute(App.LIGHTS);
	}

	private void toggleLight(Light light) {
		new DataPUT(mContext).execute(App.LIGHTS, "toggle=" + ((Hue)light).getUID());
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

	private class LightsGET extends DataGET {

		public LightsGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mList = new ArrayList<>();
			mRoomList = new ArrayList<>();
			try {
				JSONObject nJson = new JSONObject(result);
				JSONArray arr = nJson.getJSONArray("lights");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject json = arr.getJSONObject(i);
					Light nLight = new Hue(json);
					mList.add(nLight);
					
				}
				JSONArray nArr = nJson.getJSONArray("rooms");
				for (int i = 0; i < nArr.length(); i++) {
					Room nRoom = new Room(nArr.getJSONObject(i));
					mRoomList.add(nRoom);
				}
			} catch (JSONException e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
			setListAdapter(new LightAdapter(mContext, mList, mRoomList));
			setItemListener();
		}

	}

}
