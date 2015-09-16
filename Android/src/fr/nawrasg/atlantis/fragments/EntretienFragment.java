package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.EntretienAdapter;
import fr.nawrasg.atlantis.async.DataDELETE;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Entretien;

public class EntretienFragment extends ListFragment {
	private Context mContext;
	private EntretienAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		getActivity().getActionBar().setIcon(R.drawable.ng_soap);
		View nV = inflater.inflate(R.layout.layout_entretien, container, false);
		if (getActivity().findViewById(R.id.main_fragment2) == null) {
			setHasOptionsMenu(true);
		}
		return nV;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_entretien, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemEntretienAdd:
				((MainFragmentActivity) getActivity()).loadFragment(new EntretienAddFragment(), true);
				return true;
		}
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		getItems();			
	}

	public void getItems() {
		new EntretienGET(mContext).execute(App.ENTRETIEN);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_entretien, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		Entretien nEntretien = mAdapter.getItem(position);
		switch (item.getItemId()) {
			case R.id.itemEntretienPlus:
				new EntretienPUT(mContext, nEntretien, '+').execute(App.ENTRETIEN, "id=" + nEntretien.getID() + "&qte=" + (nEntretien.getQuantity() + 1));
				break;
			case R.id.itemEntretienMinus:
				new EntretienPUT(mContext, nEntretien, '-').execute(App.ENTRETIEN, "id=" + nEntretien.getID() + "&qte=" + (nEntretien.getQuantity() - 1));
				break;
			case R.id.itemEntretienDel:
				new EntretienDELETE(mContext, nEntretien).execute(App.ENTRETIEN, "id=" + nEntretien.getID());
				break;
		}
		return super.onContextItemSelected(item);
	}

	private class EntretienGET extends DataGET {

		public EntretienGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			ArrayList<Entretien> nList = new ArrayList<Entretien>();
			try {
				JSONArray arr = new JSONArray(result);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject jdata = arr.getJSONObject(i);
					Entretien nMed = new Entretien(jdata);
					nList.add(nMed);
				}
				mAdapter = new EntretienAdapter(mContext, nList);
				setListAdapter(mAdapter);
			} catch (JSONException e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

	}

	private class EntretienPUT extends DataPUT {
		private Entretien mEntretien;
		private char mMode;

		public EntretienPUT(Context context, Entretien entretien, char mode) {
			super(context);
			mEntretien = entretien;
			mMode = mode;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				switch(mMode){
					case '+':
						mEntretien.increment();
						mAdapter.notifyDataSetChanged();
						break;
					case '-':
						mEntretien.decrement();
						mAdapter.notifyDataSetChanged();
						break;
				}
			}
			super.onPostExecute(result);
		}

	}

	private class EntretienDELETE extends DataDELETE{
		private Entretien mEntretien;

		public EntretienDELETE(Context context, Entretien entretien) {
			super(context);
			mEntretien = entretien;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				mAdapter.remove(mEntretien);
			}
			super.onPostExecute(result);
		}
	}

}