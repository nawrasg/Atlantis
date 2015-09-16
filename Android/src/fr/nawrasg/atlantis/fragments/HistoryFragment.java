package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;

public class HistoryFragment extends Fragment {
	private Context mContext;
	private LineChart mLineChart;
	private Spinner spRoom, spType;
	private ArrayList<String> mRoomList, mTypeList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_history, container, false);
		getActivity().getActionBar().setIcon(R.drawable.ng_graph);
		mContext = getActivity();
		mLineChart = (LineChart) nView.findViewById(R.id.chartHistory);
		spRoom = (Spinner) nView.findViewById(R.id.spHistoryRoom);
		spType = (Spinner) nView.findViewById(R.id.spHistorySensor);
		DisplayMetrics nMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(nMetrics);
		int nHeight = nMetrics.heightPixels;
		ViewGroup.LayoutParams nParams = mLineChart.getLayoutParams();
		nParams.height = nHeight * 2 / 3;
		mLineChart.setLayoutParams(nParams);
		mLineChart.setDescription("Historique Atlantis");
		// mLineChart.setStartAtZero(false);
		mLineChart.setNoDataTextDescription("Pas de données disponibles !");
		// mLineChart.setDrawYValues(false);
		// mLineChart.setBackgroundColor(Color.GRAY);
		mLineChart.setDragEnabled(true);
		mLineChart.setScaleEnabled(true);
		mLineChart.setDrawGridBackground(false);
		mLineChart.setDrawVerticalGrid(false);
		mLineChart.setDrawHorizontalGrid(false);
		setHasOptionsMenu(true);
		new InitProgress(mContext).execute(App.HISTORY, "init");
		return nView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_history, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemHistoryRefresh:
				new MeanProgress(mContext).execute(App.HISTORY, "mean&day=5&room=" + spRoom.getSelectedItem().toString()
						+ "&type=" + spType.getSelectedItem().toString());
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class InitProgress extends DataGET {

		public InitProgress(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mRoomList = new ArrayList<String>();
			mTypeList = new ArrayList<String>();
			try {
				JSONObject nResult = new JSONObject(result);
				JSONArray nRooms = nResult.getJSONArray("rooms");
				JSONArray nType = nResult.getJSONArray("type");
				for (int i = 0; i < nRooms.length(); i++) {
					String nTemp = nRooms.getJSONObject(i).getString("room");
					mRoomList.add(nTemp);
				}
				for (int i = 0; i < nType.length(); i++) {
					String nTemp = nType.getJSONObject(i).getString("type");
					mTypeList.add(nTemp);
				}
				ArrayAdapter<String> nAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item,
						mRoomList);
				nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spRoom.setAdapter(nAdapter);
				nAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mTypeList);
				nAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spType.setAdapter(nAdapter);

			} catch (Exception e) {
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class MeanProgress extends DataGET {
		ArrayList<Entry> valsComp = new ArrayList<Entry>();
		ArrayList<String> xVals = new ArrayList<String>();

		public MeanProgress(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject json = new JSONObject(result);
				JSONArray nArr = json.getJSONArray("values");
				for (int i = 0; i < nArr.length(); i++) {
					JSONObject nTemp = nArr.getJSONObject(i);
					Entry nEntrey = new Entry(Float.parseFloat(nTemp.getString("value")), i);
					valsComp.add(nEntrey);
					xVals.add(nTemp.getString("date"));
				}
				LineDataSet set1 = new LineDataSet(valsComp, spType.getSelectedItem().toString());
				set1.setColor(ColorTemplate.getHoloBlue());
				set1.setCircleColor(ColorTemplate.getHoloBlue());
				set1.setLineWidth(2f);
				set1.setCircleSize(4f);
				set1.setFillAlpha(65);
				set1.setFillColor(ColorTemplate.getHoloBlue());
				set1.setHighLightColor(Color.rgb(244, 117, 117));
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				dataSets.add(set1);
				LineData data = new LineData(xVals, dataSets);
				mLineChart.setUnit(" " + json.getString("unit"));
				mLineChart.setData(data);
				Legend l = mLineChart.getLegend();
				l.setForm(LegendForm.LINE);
				l.setTextColor(Color.BLACK);
				XLabels xl = mLineChart.getXLabels();
				xl.setTextColor(Color.BLACK);
				YLabels yl = mLineChart.getYLabels();
				yl.setTextColor(Color.BLACK);
				mLineChart.setDescription("Capteur : " + json.getString("sensor") + " (" + json.getString("device") + ")");
				mLineChart.animateX(2500);
			} catch (Exception e) {
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	}

}
