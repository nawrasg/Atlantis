package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Hue;
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.Room;

public class LightLegacyAdapter extends ArrayAdapter<Light> {
	private Context mContext;
	private List<Light> mList;
	private List<Room> mRoomList;
	private Light mLight;

	static class LightViewHolder {
		@Bind(R.id.imgLightIcon)
		ImageView imgLightIcon;
		@Bind(R.id.lblLightName)
		TextView lblLightName;
		@Bind(R.id.swtLightToggle)
		Switch swtLightToggle;
		@Bind(R.id.sbLightIntensity)
		SeekBar sbLightIntensity;

		public LightViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public LightLegacyAdapter(Context context, List<Light> objects) {
		super(context, R.layout.row_light_legacy, objects);
		mContext = context;
		mList = objects;
	}

	public LightLegacyAdapter(Context context, List<Light> objects, List<Room> rooms) {
		super(context, R.layout.row_light_legacy, objects);
		mContext = context;
		mList = objects;
		mRoomList = rooms;
	}
	
	private String getRoom(Light light) {
		Room nRoom;
		if (mRoomList != null) {
			for (int i = 0; i < mRoomList.size(); i++) {
				nRoom = mRoomList.get(i);
				if (light.getRoom() != null && light.getRoom().equals(nRoom.getID())) {
					return " (" + nRoom.getRoom() + ")";
				}
			}
		}
		return "";
	}

	private boolean getPowerStatus(Light light) {
		Hue nLight = (Hue)light;
		if(nLight.isReachable()){
			return nLight.isOn();
		}else{
			return false;			
		}
	}

	private void setPowerStatus(Light light, boolean status) {
		String nURL = App.getUri(mContext, App.LIGHTS) + "&on=" + ((Hue)light).getUID() + "&protocol=" + light.getProtocol() + "&value=" + status;
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

			}
		});
	}

	private int getLightIntensity(Light light) {
		switch(light.getProtocol()){
			case Light.HUE:
				return ((Hue)light).getBrightness();
		}
		return 0;
	}

	private void setLightIntensity(Light light, int value) {
		String nURL = App.getUri(mContext, App.LIGHTS) + "&bri=" + ((Hue)light).getUID() + "&protocol=" + light.getProtocol() + "&value=" + value;
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

			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mLight = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_light_legacy, parent, false);
			final LightViewHolder nHolder = new LightViewHolder(nView);
			nHolder.sbLightIntensity.setTag(mLight);
			nHolder.sbLightIntensity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Light nLight = (Hue)nHolder.sbLightIntensity.getTag();
					setLightIntensity(nLight, seekBar.getProgress());
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				}
			});
			nHolder.swtLightToggle.setTag(mLight);
			nHolder.swtLightToggle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Light nLight = (Hue)v.getTag();
					setPowerStatus(nLight, nHolder.swtLightToggle.isChecked());
				}
			});
			nView.setTag(nHolder);
		}

		LightViewHolder nHolder = (LightViewHolder) nView.getTag();
		String nName = mLight.getName();
		nName += getRoom(mLight);
		nHolder.lblLightName.setText(nName);
		switch (mLight.getProtocol()) {
			case Light.HUE:
				nHolder.imgLightIcon.setImageResource(R.drawable.ng_bulb);
				nHolder.swtLightToggle.setChecked(getPowerStatus(mLight));
				int nValue = getLightIntensity(mLight);
				nHolder.sbLightIntensity.setProgress(nValue);
				break;
		}
		return nView;
	}
}
