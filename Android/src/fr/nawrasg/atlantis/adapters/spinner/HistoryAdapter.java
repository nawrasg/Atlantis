package fr.nawrasg.atlantis.adapters.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.PDevice;
import fr.nawrasg.atlantis.type.Plant;
import fr.nawrasg.atlantis.type.Sensor;

/**
 * Created by Nawras GEORGI on 07/10/2015.
 */
public class HistoryAdapter extends ArrayAdapter<PDevice> {
	private Context mContext;
	private List<PDevice> mList;
	private PDevice mDevice;

	static class HistoryViewHolder {
		@Bind(R.id.lblHistoryRowTitle)
		TextView lblTitle;

		public HistoryViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public HistoryAdapter(Context context, List<PDevice> objects) {
		super(context, R.layout.row_history, objects);
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
		PDevice nDevice = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_history, parent, false);
			HistoryViewHolder nHolder = new HistoryViewHolder(nView);
			nView.setTag(nHolder);
		}
		HistoryViewHolder nHolder = (HistoryViewHolder) nView.getTag();
		if (nDevice instanceof Plant) {
			Plant nPlant = (Plant) nDevice;
			nHolder.lblTitle.setText(nPlant.getTitle());
			nHolder.lblTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_plant, 0, 0, 0);
		} else if (nDevice instanceof Sensor) {
			Sensor nSensor = (Sensor) nDevice;
			nHolder.lblTitle.setText(nSensor.getAlias() + " (" + nSensor.getType() + ")");
			nHolder.lblTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_device, 0, 0, 0);
		}
		return nView;
	}
}
