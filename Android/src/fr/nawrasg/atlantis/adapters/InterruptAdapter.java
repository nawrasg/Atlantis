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
import fr.nawrasg.atlantis.type.Light;
import fr.nawrasg.atlantis.type.PDevice;
import fr.nawrasg.atlantis.type.Sensor;

public class InterruptAdapter extends ArrayAdapter<PDevice> {
	private Context mContext;
	private List<PDevice> mList;
	private PDevice mDevice;

	static class InterruptViewHolder {
		@Bind(R.id.imgInterruptIcon)
		ImageView imgInterruptIcon;
		@Bind(R.id.lblInterruptName)
		TextView lblInterruptName;

		public InterruptViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public InterruptAdapter(Context context, List<PDevice> objects) {
		super(context, 0, objects);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mDevice = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_actionner, parent, false);
			final InterruptViewHolder nHolder = new InterruptViewHolder(nView);
			nView.setTag(nHolder);
		}
		InterruptViewHolder nHolder = (InterruptViewHolder) nView.getTag();
		switch (mDevice.getType()) {
			case "light":
				nHolder.lblInterruptName.setText(((Light) mDevice).getName());
				nHolder.imgInterruptIcon.setImageResource(R.drawable.ng_bulb);
				break;
			default:
				nHolder.lblInterruptName.setText(((Sensor) mDevice).getSensor());
				nHolder.imgInterruptIcon.setImageResource(R.drawable.ng_device);
		}
		return nView;
	}

}
