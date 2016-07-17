package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.android.datetimepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.adapters.spinner.HistoryAdapter;
import fr.nawrasg.atlantis.type.PDevice;
import fr.nawrasg.atlantis.type.Plant;
import fr.nawrasg.atlantis.type.Sensor;

public class HistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
	private Context mContext;
	@Bind(R.id.btnHistoryFrom)
	Button btnFrom;
	@Bind(R.id.btnHistoryTo)
	Button btnTo;
	@Bind(R.id.chartHistory)
	LineChart mLineChart;
	@Bind(R.id.spHistory)
	Spinner spHistory;
	private HistoryAdapter mAdapter;
	private ArrayList<PDevice> mList;
	private Calendar mCalendar;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_history, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		mHandler = new Handler();
		mCalendar = Calendar.getInstance();
		DisplayMetrics nMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(nMetrics);
		int nHeight = nMetrics.heightPixels;
		ViewGroup.LayoutParams nParams = mLineChart.getLayoutParams();
		nParams.height = nHeight * 2 / 3;
		mLineChart.setLayoutParams(nParams);
		mLineChart.setDescription(getResources().getString(R.string.fragment_history_chart_description));
		mLineChart.setNoDataTextDescription(getResources().getString(R.string.fragment_history_chart_no_data));
		mLineChart.setDragEnabled(true);
		mLineChart.setScaleEnabled(true);
		mLineChart.setDrawGridBackground(false);
		setHasOptionsMenu(true);
		spHistory.setOnItemSelectedListener(this);
		getSensors();
		return nView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_history, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemHistoryRefresh:
				int position = spHistory.getSelectedItemPosition();
				PDevice nDevice = mList.get(position);
				loadHistory(nDevice);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		PDevice nDevice = mList.get(position);
		loadHistory(nDevice);
	}

	private void loadHistory(PDevice device) {
		if (device instanceof Sensor) {
			loadSensor((Sensor) device);
		} else if (device instanceof Plant) {
			loadPlant((Plant) device);
		}
	}

	private void loadPlant(Plant plant) {
		String nURL = "plant=" + plant.getId();
		if (!btnFrom.getText().equals("De")) {
			nURL += "&from=" + btnFrom.getText();
		}
		if (!btnTo.getText().equals("Jusqu'à")) {
			nURL += "&to=" + btnTo.getText();
		}
		getPlant(nURL);
	}

	private void loadSensor(Sensor sensor) {
		String nURL = "sensor=" + sensor.getID();
		if (!btnFrom.getText().equals("De")) {
			nURL += "&from=" + btnFrom.getText();
		}
		if (!btnTo.getText().equals("Jusqu'à")) {
			nURL += "&to=" + btnTo.getText();
		}
		getSensor(nURL);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@OnClick(R.id.btnHistoryFrom)
	public void setFrom() {
		DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "FromDate");
	}

	@OnClick(R.id.btnHistoryTo)
	public void setTo() {
		DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "ToDate");
	}

	@Override
	public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
		switch (dialog.getTag()) {
			case "FromDate":
				btnFrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				break;
			case "ToDate":
				btnTo.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				break;
		}
	}

	private void getSensors(){
		String nURL = App.getFullUrl(mContext) + App.HISTORY + "?api=" + App.getAPI(mContext);
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				mList = new ArrayList<>();
				try {
					JSONObject nJson = new JSONObject(response.body().string());
					JSONArray nPlantArr = nJson.getJSONArray("plants");
					for (int i = 0; i < nPlantArr.length(); i++) {
						Plant nPlant = new Plant(nPlantArr.getJSONObject(i));
						mList.add(nPlant);
					}
					JSONArray nSensorArr = nJson.getJSONArray("sensors");
					for (int i = 0; i < nSensorArr.length(); i++) {
						Sensor nSensor = new Sensor(nSensorArr.getJSONObject(i));
						mList.add(nSensor);
					}
					mAdapter = new HistoryAdapter(mContext, mList);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							spHistory.setAdapter(mAdapter);
						}
					});
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}

	private void getSensor(String data){
		String nURL = App.getFullUrl(mContext) + App.HISTORY + "?api=" + App.getAPI(mContext) + "&" + data;
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				ArrayList<Entry> valsComp = new ArrayList<Entry>();
				ArrayList<String> xVals = new ArrayList<String>();
				try {
					JSONArray nArr = new JSONArray(response.body().string());
					for (int i = 0; i < nArr.length(); i++) {
						JSONObject nTemp = nArr.getJSONObject(i);
						Entry nEntrey = new Entry(Float.parseFloat(nTemp.getString("value")), i);
						valsComp.add(nEntrey);
						xVals.add(nTemp.getString("date"));
					}
					LineDataSet set1 = new LineDataSet(valsComp, "");
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
					mLineChart.setData(data);
					Legend l = mLineChart.getLegend();
					l.setForm(Legend.LegendForm.LINE);
					l.setTextColor(Color.BLACK);
					mLineChart.setDescription("Capteur");
					mLineChart.animateX(2500);
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}

	private void getPlant(String data){
		String nURL = App.getFullUrl(mContext) + App.HISTORY + "?api=" + App.getAPI(mContext) + "&" + data;
		Request nRequest = new Request.Builder().url(nURL).build();
		App.httpClient.newCall(nRequest).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}

			@Override
			public void onResponse(Response response) throws IOException {
				ArrayList<Entry> valsComp = new ArrayList<Entry>();
				ArrayList<String> xVals = new ArrayList<String>();
				try {
					JSONArray nArr = new JSONArray(response.body().string());
					for (int i = 0; i < nArr.length(); i++) {
						JSONObject nTemp = nArr.getJSONObject(i);
						Entry nEntrey = new Entry(Float.parseFloat(nTemp.getString("moisture_m")), i);
						valsComp.add(nEntrey);
						xVals.add(nTemp.getString("date"));
					}
					LineDataSet set1 = new LineDataSet(valsComp, "");
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
					mLineChart.setData(data);
					Legend l = mLineChart.getLegend();
					l.setForm(Legend.LegendForm.LINE);
					l.setTextColor(Color.BLACK);
					mLineChart.setDescription("Plante");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mLineChart.animateX(2500);
						}
					});
				} catch (JSONException e) {
					Log.e("Atlantis", e.toString());
				}
			}
		});
	}
}
