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
import fr.nawrasg.atlantis.adapters.PharmacieAdapter;
import fr.nawrasg.atlantis.async.DataDELETE;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Medicament;

public class PharmacieFragment extends ListFragment {
	private Context mContext;
	private ArrayList<Medicament> nList;
	private PharmacieAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		getActivity().getActionBar().setIcon(R.drawable.pharmacie);
		View nV = inflater.inflate(R.layout.layout_pharmacie, container, false);
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
		inflater.inflate(R.menu.fragment_pharmacie, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemPharmacieAdd:
				((MainFragmentActivity) getActivity()).loadFragment(new PharmacieAddFragment(), true);
				return true;
		}
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		getItems();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_pharmacie, menu);
	}

	public void getItems() {
		new PharmacieGET(mContext).execute(App.PHARMACIE);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		Medicament nMedicament = nList.get(position);
		switch (item.getItemId()) {
			case R.id.itemPharmaciePlus:
				new PharmaciePUT(mContext, nMedicament, '+').execute(App.PHARMACIE, "id=" + nMedicament.getID() + "&qte=" + (nMedicament.getQuantity() + 1));
				return true;
			case R.id.itemPharmacieMinus:
				new PharmaciePUT(mContext, nMedicament, '-').execute(App.PHARMACIE, "id=" + nMedicament.getID() + "&qte=" + (nMedicament.getQuantity() - 1));
				return true;
			case R.id.itemPharmacieDel:
				new PharmacieDELETE(mContext, nMedicament).execute(App.PHARMACIE, "id=" + nMedicament.getID());
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private class PharmacieGET extends DataGET{

		public PharmacieGET(Context context) {
			super(context);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				nList = new ArrayList<Medicament>();
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jdata = arr.getJSONObject(i);
						Medicament nMed = new Medicament(jdata);
						nList.add(nMed);
					}
					mAdapter = new PharmacieAdapter(mContext, nList);
					setListAdapter(mAdapter);
				} catch (JSONException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}
		
	}

	private class PharmaciePUT extends DataPUT {
		private Medicament mMedicament;
		private char mMode;

		public PharmaciePUT(Context context, Medicament medicament, char mode) {
			super(context);
			mMedicament = medicament;
			mMode = mode;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				switch(mMode){
					case '+':
						mMedicament.increment();
						mAdapter.notifyDataSetChanged();
						break;
					case '-':
						mMedicament.decrement();
						mAdapter.notifyDataSetChanged();
						break;
				}
			}
			super.onPostExecute(result);
		}
	}

	private class PharmacieDELETE extends DataDELETE{
		private Medicament mMedicament;

		public PharmacieDELETE(Context context, Medicament medicament) {
			super(context);
			mMedicament = medicament;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				mAdapter.remove(mMedicament);
			}
			super.onPostExecute(result);
		}
	}

}