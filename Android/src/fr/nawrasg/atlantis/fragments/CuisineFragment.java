package fr.nawrasg.atlantis.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;

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
import fr.nawrasg.atlantis.activities.MainFragmentActivity;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.CuisineAdapter;
import fr.nawrasg.atlantis.type.Produit;

public class CuisineFragment extends ListFragment {
	private Context mContext;
	private ArrayList<Produit> nList;
	private CuisineAdapter mAdapter;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mHandler = new Handler();
		View nView = inflater.inflate(R.layout.fragment_cuisine, container, false);
		setHasOptionsMenu(true);
		return nView;
	}
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_cuisine, menu);
		SearchView nSV = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.itemSearch));
		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				if (mAdapter != null)
					mAdapter.getFilter().filter(newText);
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		};
		nSV.setOnQueryTextListener(queryTextListener);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem nSeeAllItem = menu.findItem(R.id.itemCuisineAll);
		if(mAdapter != null && mAdapter.isShowAll()){
			nSeeAllItem.setChecked(true);
		}else{
			nSeeAllItem.setChecked(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemCuisineAdd:
				((MainFragmentActivity) getActivity()).loadFragment(new CuisineAddFragment(), true);
				break;
			case R.id.itemCuisineAll:
				mAdapter.toggleShow();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onResume() {
		super.onResume();
		getItems();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater nMI = getActivity().getMenuInflater();
		nMI.inflate(R.menu.context_cuisine, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		Produit nProduit = mAdapter.getItem(position);
		switch (item.getItemId()) {
			case R.id.itemCuisineOpen:
				modifyItem(nProduit, ',');
				return true;
			case R.id.itemCuisinePlus:
				modifyItem(nProduit, '+');
				return true;
			case R.id.itemCuisineMinus:
				modifyItem(nProduit, '-');
				return true;
			case R.id.itemCuisineDel:
				deleteItem(nProduit);
				return true;
			case R.id.itemCuisineAvoid:
				modifyItem(nProduit, '.');
				return true;
		}
		return super.onContextItemSelected(item);
	}

	public void getItems() {
		String nURL = App.getUri(mContext, App.CUISINE);
		Request nRequest = new Request.Builder()
				.url(nURL)
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				//TODO
			}

			@Override
			public void onResponse(Response response) throws IOException {
				if(response.code() == 202){
					nList = new ArrayList<>();
					try {
						JSONArray nArr = new JSONArray(response.body().string());
						for(int i = 0; i < nArr.length(); i++){
							JSONObject nJson = nArr.getJSONObject(i);
							Produit nProduit = new Produit(nJson);
							nList.add(nProduit);
						}
						mAdapter = new CuisineAdapter(mContext, nList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								setListAdapter(mAdapter);
							}
						});
					}catch(JSONException e){
						Log.e("Atlantis", e.getMessage());
					}
				}
			}
		});
	}

	private void modifyItem(final Produit produit, final char mode){
		String nURL = App.getUri(mContext, App.CUISINE);
		switch(mode){
			case ',':
				nURL += "&open=" + produit.getID();
				break;
			case '+':
				nURL += "&id=" + produit.getID() + "&quantite=" + (produit.getQuantite() + 1);
				break;
			case '-':
				nURL += "&id=" + produit.getID() + "&quantite=" + (produit.getQuantite() - 1);
				break;
			case '.':
				nURL += "&ignore=" + produit.getID();
				break;
		}
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
				if(response.code() == 202){
					switch(mode){
						case ',':
							produit.open();
							break;
						case '+':
							produit.increment();
							break;
						case '-':
							produit.decrement();
							break;
						case '.':
							produit.ignore();
							break;
					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}

	private void deleteItem(final Produit produit){
		String nURL = App.getUri(mContext, App.CUISINE) + "&id=" + produit.getID();
		Request nRequest = new Request.Builder()
				.url(nURL)
				.delete()
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				Log.d("Nawras", response.code() + "");
				if(response.code() == 202){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mAdapter.remove(produit);
						}
					});
				}
			}
		});
	}
}