package fr.nawrasg.atlantis.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.adapters.CoursesAdapter;
import fr.nawrasg.atlantis.adapters.CoursesAdapterLegacy;
import fr.nawrasg.atlantis.type.Element;

public class CoursesLegacyFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
	@Bind(R.id.swipeCourses)
	SwipeRefreshLayout mSwipeLayout;
	private Context mContext;
	private List<String> list = new ArrayList<String>();
	private ArrayList<Element> nList;
	private boolean mIsOffline;
	private CoursesAdapterLegacy mAdapter;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_courses, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		mHandler = new Handler();
		setHasOptionsMenu(true);
		mSwipeLayout.setOnRefreshListener(this);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((MainActivity)getActivity()).setProgressBar(true);
		getItems();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_courses, menu);
		MenuItem nItem = menu.findItem(R.id.itemCoursesOffline);
		nItem.setCheckable(true);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_courses, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemCoursesAdd:
				addItem();
				return true;
			case R.id.itemCoursesNotify:
				sendNotification();
				return true;
			case R.id.itemCoursesOffline:
				if (mIsOffline) {
					makeOnline();
				} else {
					makeOffline();
				}
				return true;
			case R.id.itemCoursesClear:
				clear();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem nItem = menu.findItem(R.id.itemCoursesOffline);
		if (mIsOffline) {
			nItem.setChecked(true);
		} else {
			nItem.setChecked(false);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		Element nElement = mAdapter.getItem(position);
		switch (item.getItemId()) {
			case R.id.itemCoursesPlus:
				modifyCourses(nElement, '+');
				return true;
			case R.id.itemCoursesMinus:
				modifyCourses(nElement, '-');
				return true;
			case R.id.itemCoursesDel:
				deleteCourses(nElement);
				return true;
		}
		return super.onContextItemSelected(item);
	}

	private void modifyCourses(final Element element, final char mode){
		String nURL = App.getUri(mContext, App.COURSES) + "&id=" + element.getID();
		switch(mode){
			case '+':
				nURL += "&quantity=" + (element.getQuantity() + 1);
				break;
			case '-':
				nURL += "&quantity=" + (element.getQuantity() - 1);
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
						case '+':
							element.increment();
							break;
						case '-':
							element.decrement();
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

	private void makeOnline() {
		SharedPreferences nPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor nEdit = nPrefs.edit();
		nEdit.remove("CoursesSet");
		nEdit.commit();
		getItems();
	}

	private void makeOffline() {
		Element nElement;
		Set<String> nSet = new HashSet<>();
		String nRow;
		for (int i = 0; i < nList.size(); i++) {
			nElement = nList.get(i);
			nRow = nElement.getName() + "," + nElement.getQuantity();
			nSet.add(nRow);
		}
		SharedPreferences nPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor nEdit = nPrefs.edit();
		nEdit.putStringSet("CoursesSet", nSet);
		nEdit.commit();
		getItems();
	}

	private void getItems() {
		SharedPreferences nPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Set<String> nSet = nPrefs.getStringSet("CoursesSet", null);
		if (nSet == null || nSet.size() == 0) {
			mIsOffline = false;
			getCourses();
		} else {
			mIsOffline = true;
			Iterator<String> nIterator = nSet.iterator();
			nList = new ArrayList<Element>();
			while (nIterator.hasNext()) {
				String nRow = nIterator.next();
				String[] nData = nRow.split(",");
				Element nElement = new Element(nData[0], nData[1]);
				nList.add(nElement);
			}
			mAdapter = new CoursesAdapterLegacy(mContext, nList);
			setListAdapter(mAdapter);
		}
	}

	private void getCourses(){
		String nURL = App.getUri(mContext, App.COURSES);
		Request nRequest = new Request.Builder()
				.url(nURL)
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				nList = new ArrayList<Element>();
				try {
					JSONArray arr = new JSONArray(response.body().string());
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jdata = arr.getJSONObject(i);
						Element nElement = new Element(jdata);
						nList.add(nElement);
					}
					mAdapter = new CoursesAdapterLegacy(mContext, nList);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							setListAdapter(mAdapter);
							mSwipeLayout.setRefreshing(false);
							((MainActivity)getActivity()).setProgressBar(false);
						}
					});
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}

	public void clear() {
		deleteCourses(null);
		makeOnline();
	}

	public void sendNotification() {
		try {
			String nMsg = URLEncoder.encode(getResources().getString(R.string.fragment_courses_notification), "UTF-8");
			String nURL = App.getUri(mContext, App.NOTIFY) + "&msg=" + nMsg;
			Request nRequest = new Request.Builder()
					.url(nURL)
					.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
					.build();
			App.httpClient.newCall(nRequest).enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {

				}

				@Override
				public void onResponse(Response response) throws IOException {

				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void addItem() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_courses, null);
		AlertDialog.Builder inputBoxBuilder = new AlertDialog.Builder(mContext);
		inputBoxBuilder.setView(view);
		final AutoCompleteTextView txtAdd = (AutoCompleteTextView) view.findViewById(R.id.txtAutoAdd);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
		txtAdd.setAdapter(adapter);
		inputBoxBuilder.setPositiveButton(getResources().getString(R.string.fragment_courses_button_add), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String res = txtAdd.getText().toString();
				String result[] = res.split(",");
				if (result.length > 1) {
					int n = result.length;
					result[n - 1] = result[n - 1].replace(" ", "");
					try {
						Long l = Long.parseLong(result[n - 1]);
						String name = result[0];
						for (int i = 1; i < n - 2; i++) {
							name += result[i];
						}
						name = URLEncoder.encode(name, "UTF-8");
						postCourses(name, l);
					} catch (Exception e) {
						Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				} else {
					try {
						res = URLEncoder.encode(res, "UTF-8");
						postCourses(res, 1);
					} catch (Exception f) {
						Toast.makeText(mContext, f.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}
		}).setNegativeButton(getResources().getString(R.string.fragment_courses_button_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).setTitle(getResources().getString(R.string.fragment_courses_add_product));
		AlertDialog inputBox = inputBoxBuilder.create();
		inputBox.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		inputBox.show();
	}

	private void postCourses(String name, long quantity){
		String nURL = App.getUri(mContext, App.COURSES) + "&name=" + name + "&quantity=" + quantity;
		Request nRequest = new Request.Builder()
				.url(nURL)
				.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
				.build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				if(response.code() == 202){
					try{
						JSONObject nItem = new JSONObject(response.body().string());
						final Element nElement = new Element(nItem);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								mAdapter.add(nElement);
							}
						});
					}catch(JSONException e){

					}
				}
			}
		});
	}

	private void deleteCourses(final Element element){
		String nURL = App.getUri(mContext, App.COURSES);
		if(element != null){
			nURL += "&id=" + element.getID();
		}
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
				if(response.code() == 202){
					if(element != null){
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								mAdapter.remove(element);
							}
						});
					}else{
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								mAdapter.clear();
							}
						});
					}
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		makeOnline();
	}
}
