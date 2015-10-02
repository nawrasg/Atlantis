package fr.nawrasg.atlantis.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.async.DataGET;
import fr.nawrasg.atlantis.async.DataPUT;

public class HomeFragment extends Fragment implements OnTouchListener {
	private Context mContext;
	@Bind(R.id.imgPlanPlan)
	ImageView imgPlan;
	@Bind(R.id.txtHomeWeatherToday)
	TextView txtWeatherToday;
	@Bind(R.id.txtHomeWeatherTomorrow)
	TextView txtWeatherTomorrow;
	@Bind(R.id.swtPlanAlarm)
	Switch swtAlarm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nView = inflater.inflate(R.layout.fragment_plan, container, false);
		ButterKnife.bind(this, nView);
		mContext = getActivity();
		return nView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// imgPlan.setOnTouchListener(this);
		new HomeGET(mContext).execute(App.HOME);
		loadPlan();
	}

	private void loadPlan() {
		Picasso.with(mContext).load(App.getFullUrl(mContext) + App.PLAN).into(imgPlan);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Drawable nDrawable = imgPlan.getDrawable();
		Bitmap nBitmap = ((BitmapDrawable) nDrawable).getBitmap();
		if (nBitmap == null) {
			return false;
		}
		int nWidth = imgPlan.getWidth();
		int nHeight = imgPlan.getHeight();
		if (nWidth > nHeight) {
			int nScale = nBitmap.getHeight() / nHeight;

		} else {
			int nScale = nBitmap.getWidth() / nWidth;
		}
		Log.d("Nawras", nBitmap.getWidth() + " " + nBitmap.getHeight());

		int[] viewCoords = new int[2];
		imgPlan.getLocationInWindow(viewCoords);
		int touchX = (int) event.getX();
		int touchY = (int) event.getY();

		int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X
												// coordinate
		int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y
												// coordinate
		// Toast.makeText(mContext, imageX + " " + imageY,
		// Toast.LENGTH_SHORT).show();
		return true;
	}

	@OnClick(R.id.swtPlanAlarm)
	private void toggleAlarm() {
		new DataPUT(mContext).execute(App.HOME, "alarm=" + swtAlarm.isChecked());
	}

	private void setWeather(TextView view, JSONObject json) throws JSONException {
		String nCode = json.getString("code");
		double nTemp = json.getDouble("temperature");
		String nDescription = json.getString("description");
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

	private class HomeGET extends DataGET {

		public HomeGET(Context context) {
			super(context);
		}

		@Override
		protected void onPostExecute(String result) {
			if(getResultCode() == 202){
				try {
					JSONObject nJson = new JSONObject(result);
					boolean nAlarm = nJson.getBoolean("alarm");
					if (nAlarm) {
						swtAlarm.setChecked(true);
					} else {
						swtAlarm.setChecked(false);
					}
					JSONArray nArr = nJson.getJSONArray("weather");
					JSONObject nJsonC = nArr.getJSONObject(0);
					setWeather(txtWeatherToday, nJsonC);
					nJsonC = nArr.getJSONObject(1);
					setWeather(txtWeatherTomorrow, nJsonC);
				} catch (JSONException e) {
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}

	}

}
