package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Entretien;

public class EntretienAdapter extends ArrayAdapter<Entretien> {
	private Context mContext;
	private ArrayList<Entretien> mList;
	private Entretien mEntretien;

	static class EntretienViewHolder {
		@Bind(R.id.imgEntretienIcon)
		ImageView icon;
		@Bind(R.id.imgEntretienIcon2)
		ImageView icon2;
		@Bind(R.id.lblEntretienTitle)
		TextView title;
		@Bind(R.id.lblEntretienDescription)
		TextView description;
		@Bind(R.id.lblEntretienQuantity)
		TextView quantity;

		public EntretienViewHolder(View view){
			ButterKnife.bind(this, view);
		}
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
			final EntretienViewHolder nHolder = new EntretienViewHolder(nView);
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
