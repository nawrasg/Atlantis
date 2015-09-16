package fr.nawrasg.atlantis.adapters.spinner;

import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;

public class DeviceTypeAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] mList;

	static class DeviceTypeViewHolder {
		public TextView title;
	}

	public DeviceTypeAdapter(Context context, String[] objects) {
		super(context, R.layout.row_device_type, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}

	private View getRow(int position, View convertView, ViewGroup parent) {
		String nItem = mList[position];
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_device_type, parent, false);
			DeviceTypeViewHolder nHolder = new DeviceTypeViewHolder();
			nHolder.title = (TextView) nView.findViewById(R.id.lblDeviceTypeTitle);
			nView.setTag(nHolder);
		}
		DeviceTypeViewHolder nHolder = (DeviceTypeViewHolder) nView.getTag();
		nHolder.title.setText(nItem.substring(0, 1).toUpperCase(Locale.FRANCE) + nItem.substring(1));
		switch (nItem) {
			case "windows":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_windows, 0, 0, 0);
				break;
			case "linux":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_server, 0, 0, 0);
				break;
			case "imprimante":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_printer, 0, 0, 0);
				break;
			case "smartphone":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_smartphone, 0, 0, 0);
				break;
			default:
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_device, 0, 0, 0);
				break;
		}
		return nView;
	}

}
