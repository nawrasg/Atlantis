package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.activities.MainActivity;
import fr.nawrasg.atlantis.type.Alarm;

public class HomeFragment extends Fragment implements OnTouchListener {
	private Context mContext;
	private FloatingActionButton mFAB;
	private FloatingActionMenu mActionMenu;
	private SubActionButton mDayButton, mNightButton, mAwayButton;
	private Handler mHandler;
	private Alarm mAlarm;

	@Bind(R.id.imgPlanPlan)
	ImageView imgPlan;
	@Bind(R.id.txtHomeWeatherToday)
	TextView txtWeatherToday;
	@Bind(R.id.txtHomeWeatherTomorrow)
	TextView txtWeatherTomorrow;
	@Bind(R.id.txtHomeMode)
	TextView txtMode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_plan, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				setModeLabel(msg.getData().getString("mode"));
			}
		};
		mAlarm = new Alarm(mContext, mHandler);
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		createFAB();
		((MainActivity)getActivity()).setProgressBar(true);
		get();
		loadPlan();
	}

	private void get(){
		String nURL = App.getUri(mContext, App.HOME);
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
					try {
						JSONObject nJson = new JSONObject(response.body().string());
						JSONArray nArr = nJson.getJSONArray("weather");
						final JSONObject nJsonW1 = nArr.getJSONObject(0);
						final JSONObject nJsonW2 = nArr.getJSONObject(1);
						final String nMode = nJson.getString("mode");
						if(isAdded()){
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									setWeather(txtWeatherToday, nJsonW1);
									setWeather(txtWeatherTomorrow, nJsonW2);
									setModeLabel(nMode);
									((MainActivity)getActivity()).setProgressBar(false);
								}
							});
						}
					} catch (JSONException e) {
						//TODO
					}

				}
			}
		});
	}

	private Drawable getDraw(int resource){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return getResources().getDrawable(resource, mContext.getTheme());
		}else{
			return getResources().getDrawable(resource, null);
		}
	}

	private void createFAB() {
		ImageView nMenuIcon = new ImageView(getActivity());
		Drawable nMenuDrawable = getDraw(R.drawable.ic_dehaze_white_24dp);
		nMenuIcon.setImageDrawable(nMenuDrawable);
		mFAB = new FloatingActionButton.Builder(getActivity())
				.setContentView(nMenuIcon)
				.setTheme(FloatingActionButton.THEME_DARK)
				.build();

		ImageView nDayIcon = new ImageView(getActivity());
		Drawable nDayDrawable = getDraw(R.drawable.ic_weekend_white_18dp);
		nDayIcon.setImageDrawable(nDayDrawable);
		SubActionButton.Builder nItemBuilder = new SubActionButton.Builder(getActivity());
		mDayButton = nItemBuilder
				.setContentView(nDayIcon)
				.setTheme(SubActionButton.THEME_DARK)
				.build();
		mDayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAlarm.setMode(Alarm.MODE_DAY);
				mActionMenu.close(true);
			}
		});

		ImageView nNightIcon = new ImageView(getActivity());
		Drawable nNightDrawable = getDraw(R.drawable.ic_airline_seat_individual_suite_white_18dp);
		nNightIcon.setImageDrawable(nNightDrawable);
		mNightButton = nItemBuilder
				.setContentView(nNightIcon)
				.setTheme(SubActionButton.THEME_DARK)
				.build();
		mNightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAlarm.setMode(Alarm.MODE_NIGHT);
				mActionMenu.close(true);
			}
		});

		ImageView nAwayIcon = new ImageView(getActivity());
		Drawable nAwayDrawable = getDraw(R.drawable.ic_directions_walk_white_18dp);
		nAwayIcon.setImageDrawable(nAwayDrawable);
		mAwayButton = nItemBuilder
				.setContentView(nAwayIcon)
				.setTheme(SubActionButton.THEME_DARK)
				.build();
		mAwayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAlarm.setMode(Alarm.MODE_AWAY);
				mActionMenu.close(true);
			}
		});

		mActionMenu = new FloatingActionMenu
				.Builder(getActivity())
				.addSubActionView(mDayButton)
				.addSubActionView(mNightButton)
				.addSubActionView(mAwayButton)
				.attachTo(mFAB)
				.build();
	}

	@Override
	public void onDestroy() {
		if (mFAB != null) {
			mFAB.detach();
		}
		super.onDestroy();
	}

	private void setModeLabel(String mode){
		String nModePrefix = getResources().getString(R.string.fragment_home_mode);
		String nModePost = "";
		switch (mode) {
			case "day":
				nModePost = getResources().getString(R.string.fragment_home_mode_day);
				break;
			case "night":
				nModePost = getResources().getString(R.string.fragment_home_mode_night);
				break;
			case "away":
				nModePost = getResources().getString(R.string.fragment_home_mode_away);
				break;
		}
		txtMode.setText(nModePrefix + " " + nModePost);
	}

	private void loadPlan() {
		Picasso.with(mContext).load(App.getUri(mContext, App.Images) + "&type=plan").into(imgPlan);
		imgPlan.setOnTouchListener(this);
		drawRoom();
	}

	private void drawRoom(){
		Paint nPaint = new Paint();
		nPaint.setColor(Color.RED);
		nPaint.setStrokeWidth(6);
		nPaint.setStyle(Paint.Style.STROKE);
		Path nPath = new Path();
		nPath.moveTo(26, 26);
		nPath.lineTo(170, 26);
		nPath.lineTo(170, 200);
		nPath.lineTo(26, 200);
		Canvas nCanvas = new Canvas();
		nCanvas.drawPath(nPath, nPaint);
		imgPlan.draw(nCanvas);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return false;
		}
		Drawable nDrawable = imgPlan.getDrawable();
		Bitmap nBitmap = ((BitmapDrawable) nDrawable).getBitmap();
		if (nBitmap == null) {
			return false;
		}
		int nWidth = nBitmap.getWidth();
		int nHeight = nBitmap.getHeight();

		int[] viewCoords = new int[2];
		imgPlan.getLocationOnScreen(viewCoords);

		int touchX = (int) event.getX();
		int touchY = (int) event.getY();
		int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X
		int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y
		Log.d("Nawras", "ImageView Coords: " + viewCoords[0] + " " + viewCoords[1]);
		Log.d("Nawras", "Event Coords: " + touchX + " " + touchY);
		Log.d("Nawras", "Image Coords: " + imageX + " " + imageY);
		return true;
	}

	private void setWeather(TextView view, JSONObject json) {
		String nCode = json.optString("code");
		double nTemp = json.optDouble("temperature");
		String nDescription = json.optString("description");
		view.setText(nDescription.substring(0, 1).toUpperCase(Locale.FRANCE) + nDescription.substring(1) + " " + nTemp + "°C");
		switch (nCode) {
			case "01d":
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ng_weather_sun, 0, 0);
				break;
			case "02d":
			case "03d":
			case "04d":
			case "11d":
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ng_weather_cloud, 0, 0);
				break;
			case "09d":
			case "10d":
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ng_weather_heavy, 0, 0);
				break;
			case "13d":
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ng_weather_snow, 0, 0);
				break;
			case "50d":
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ng_weather_fog, 0, 0);
				break;
			default:
				view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
		}
	}

}
