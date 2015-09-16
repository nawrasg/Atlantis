package fr.nawrasg.atlantis.adapters.spinner;

import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;

public class ConnectionAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] mList;

	static class ConnectionViewHolder {
		public TextView title;
	}

	public ConnectionAdapter(Context context, String[] objects) {
		super(context, R.layout.row_connection, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}

	public View getRow(int position, View convertView, ViewGroup parent) {
		String nItem = mList[position];
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_connection, parent, false);
			ConnectionViewHolder nHolder = new ConnectionViewHolder();
			nHolder.title = (TextView) nView.findViewById(R.id.lblConnectionTitle);
			nView.setTag(nHolder);
		}
		ConnectionViewHolder nHolder = (ConnectionViewHolder) nView.getTag();
		nHolder.title.setText(nItem.substring(0, 1).toUpperCase(Locale.FRANCE) + nItem.substring(1));
		switch (nItem) {
			case "ethernet":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_ethernet, 0, 0, 0);
				break;
			case "wifi":
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_wifi, 0, 0, 0);
				break;
		}
		return nView;
	}

}
