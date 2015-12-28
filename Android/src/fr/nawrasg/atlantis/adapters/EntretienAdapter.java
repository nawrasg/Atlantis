package fr.nawrasg.atlantis.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Entretien;

public class EntretienAdapter extends ArrayAdapter<Entretien> {
	private Context mContext;
	private ArrayList<Entretien> mList;
	private Entretien mEntretien;

	static class EntretienViewHolder {
		public ImageView icon;
		public ImageView icon2;
		public TextView title;
		public TextView description;
		public TextView quantity;
	}

	public EntretienAdapter(Context context, ArrayList<Entretien> list) {
		super(context, R.layout.row_entretien, list);
		mContext = context;
		mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mEntretien = mList.get(position);
		View nView = convertView;
		if(nView == null){
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_entretien, parent, false);
			final EntretienViewHolder nHolder = new EntretienViewHolder();
			nHolder.title = (TextView)nView.findViewById(R.id.lblEntretienTitle);
			nHolder.description = (TextView)nView.findViewById(R.id.lblEntretienDescription);
			nHolder.quantity = (TextView)nView.findViewById(R.id.lblEntretienQuantity);
			nHolder.icon = (ImageView)nView.findViewById(R.id.imgEntretienIcon);
			nHolder.icon2 = (ImageView)nView.findViewById(R.id.imgEntretienIcon2);
			nView.setTag(nHolder);
		}
		EntretienViewHolder nHolder = (EntretienViewHolder)nView.getTag();
		nHolder.icon.setImageResource(R.drawable.ng_soap);
		nHolder.title.setText(mEntretien.getTitle());
		nHolder.quantity.setText(mEntretien.getQuantity() + "");
		long nDate = mEntretien.getDate();
		String nUnit = mEntretien.getDateUnit();
		if(nDate > 0){
			nHolder.description.setText(mContext.getResources().getString(R.string.adapter_entretien_item_date_to_use) + " " + nDate + " " + nUnit);
			nHolder.icon2.setImageDrawable(null);
		}else{
			nHolder.description.setText(mContext.getResources().getString(R.string.adapter_entretien_item_date_peremption) + " " + Math.abs(nDate) + " " + nUnit);
			nHolder.icon2.setImageResource(R.drawable.ng_ball_red);			
		}
		return nView;
	}

}
