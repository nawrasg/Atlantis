package fr.nawrasg.atlantis.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.CoursesAdapter;
import fr.nawrasg.atlantis.async.DataDELETE;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPOST;
import fr.nawrasg.atlantis.async.DataPUT;
import fr.nawrasg.atlantis.type.Element;

public class CoursesFragment extends ListFragment {
	private Context mContext;
	private List<String> list = new ArrayList<String>();
	private ArrayList<Element> nList;
	private boolean mIsOffline;
	private CoursesAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.layout_courses, container, false);
		mContext = getActivity();
		new EanGET(mContext, false).execute(App.EAN);
		setHasOptionsMenu(true);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
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
				break;
			case R.id.itemCoursesNotify:
				sendNotification();
				break;
			case R.id.itemCoursesOffline:
				if (mIsOffline) {
					makeOnline();
				} else {
					makeOffline();
				}
				break;
			case R.id.itemCoursesClear:
				clear();
				break;
			case R.id.itemCoursesRefresh:
				makeOnline();
				break;
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
				new CoursesPUT(mContext, nElement, '+').execute(App.COURSES, "quantity=" + (nElement.getQuantity() + 1) + "&id=" + nElement.getID());
				return true;
			case R.id.itemCoursesMinus:
				new CoursesPUT(mContext, nElement, '-').execute(App.COURSES, "quantity=" + (nElement.getQuantity() - 1) + "&id=" +  nElement.getID());
				return true;
			case R.id.itemCoursesDel:
				new CoursesDELETE(mContext, nElement).execute(App.COURSES, "id=" +  nElement.getID());
				return true;
		}
		return super.onContextItemSelected(item);
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
			new CoursesGET(mContext).execute(App.COURSES);
		} else {
			mIsOffline = true;
			Iterator<String> nIterator = nSet.iterator();
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			nList = new ArrayList<Element>();
			while (nIterator.hasNext()) {
				String nRow = nIterator.next();
				String[] nData = nRow.split(",");
				Element nElement = new Element(nData[0], nData[1]);
				map = nElement.getMap();
				nList.add(nElement);
				listItem.add(map);
			}
			SimpleAdapter mSchedule = new SimpleAdapter(mContext, listItem, R.layout.row, new String[] { "titre", "quantite" },
					new int[] { R.id.listTitle, R.id.listQte });
			ListView l = getListView();
			l.setAdapter(mSchedule);
		}
	}

	public void clear() {
		new CoursesDELETE(mContext).execute(App.COURSES);
		makeOnline();
	}


	public void sendNotification() {
		try {
			String nMsg = URLEncoder.encode(getResources().getString(R.string.fragment_courses_notification), "UTF-8");
			new DataPOST(mContext).execute(App.NOTIFY, "msg=" + nMsg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void addItem() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.layout_inputdialog, null);
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
						new CoursesPOST(mContext).execute(App.COURSES, "name=" + name + "&quantity=" + l);
					} catch (Exception e) {
						Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				} else {
					try {
						res = URLEncoder.encode(res, "UTF-8");
						new CoursesPOST(mContext).execute(App.COURSES, "name=" + res + "&quantity=1");
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

	private class CoursesGET extends DataGET {

		public CoursesGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			nList = new ArrayList<Element>();
			try {
				JSONArray arr = new JSONArray(result);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject jdata = arr.getJSONObject(i);
					Element nElement = new Element(jdata);
					nList.add(nElement);
				}
				mAdapter = new CoursesAdapter(mContext, nList);
				setListAdapter(mAdapter);
			} catch (JSONException e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

	private class CoursesPOST extends DataPOST {

		public CoursesPOST(Context context){ super(context);}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				try{
					JSONObject nItem = new JSONObject(result);
					Element nElement = new Element(nItem);
					mAdapter.add(nElement);
				}catch(JSONException e){

				}
			}
			super.onPostExecute(result);
		}
	}

	private class CoursesPUT extends DataPUT {
		private Element mElement;
		private char mMode;

		public CoursesPUT(Context context){super(context);}
		public CoursesPUT(Context context, Element element, char mode){
			super(context);
			mElement = element;
			mMode = mode;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				switch(mMode){
					case '+':
						mElement.increment();
						mAdapter.notifyDataSetChanged();
						break;
					case '-':
						mElement.decrement();
						mAdapter.notifyDataSetChanged();
						break;
				}
			}
			super.onPostExecute(result);
		}
	}

	private class CoursesDELETE extends DataDELETE{
		private Element mElement;

		public CoursesDELETE(Context context){
			super(context);
		}
		public CoursesDELETE(Context context, Element element){
			super(context);
			mElement = element;
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				if(mElement != null){
					mAdapter.remove(mElement);
				}else{
					mAdapter.clear();
				}
			}
			super.onPostExecute(result);
		}
	}
	
	private class EanGET extends DataGET{

		public EanGET(Context context, boolean progressbar) {
			super(context, progressbar);
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jdata = arr.getJSONObject(i);
						list.add(jdata.getString("nom"));
					}
				} catch (JSONException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}
		
	}
}
