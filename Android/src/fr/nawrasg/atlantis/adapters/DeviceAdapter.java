package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Device;

public class DeviceAdapter extends ArrayAdapter<Device> {
	private Context mContext;
	private List<Device> mDeviceList;
	private Device mDevice;

	static class DeviceViewHolder {
		@Bind(R.id.imgDeviceIcon)
		ImageView icon;
		@Bind(R.id.imgDeviceStatus)
		ImageView status;
		@Bind(R.id.imgDeviceConnectionType)
		ImageView connection;
		@Bind(R.id.lblDeviceTitle)
		TextView title;
		@Bind(R.id.lblDeviceIp)
		TextView ip;
		@Bind(R.id.lblDeviceMac)
		TextView mac;

		public DeviceViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public DeviceAdapter(Context context, List<Device> objects) {
		super(context, R.layout.row_device, objects);
		mContext = context;
		mDeviceList = objects;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mDevice = mDeviceList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_device, parent, false);
			DeviceViewHolder nHolder = new DeviceViewHolder(nView);
			nView.setTag(nHolder);
		}
		DeviceViewHolder nHolder = (DeviceViewHolder) nView.getTag();
		nHolder.title.setText(mDevice.getNom());
		nHolder.ip.setText(mDevice.getIP());
		nHolder.mac.setText(mDevice.getMac());
		switch (mDevice.getConnexion()) {
			case "ethernet":
				nHolder.connection.setImageResource(R.drawable.ng_ethernet);
				break;
			case "wifi":
				nHolder.connection.setImageResource(R.drawable.ng_wifi);
				break;
			default:
				nHolder.connection.setImageDrawable(null);
				break;
		}
		switch(mDevice.getType()){
			case "windows":
				nHolder.icon.setImageResource(R.drawable.ng_windows);
				break;
			case "linux":
				nHolder.icon.setImageResource(R.drawable.ng_server);
				break;
			case "imprimante":
				nHolder.icon.setImageResource(R.drawable.ng_printer);
				break;
			case "smartphone":
				nHolder.icon.setImageResource(R.drawable.ng_smartphone);
				break;
			default:
				nHolder.icon.setImageResource(R.drawable.ng_device);
				break;
		}
		if(mDevice.isOnline()){
			nHolder.status.setImageResource(R.drawable.ng_ball_green);
		}else{
			nHolder.status.setImageResource(R.drawable.ng_ball_red);
		}
		return nView;
	}

}
