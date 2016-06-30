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
import fr.nawrasg.atlantis.type.Medicament;

public class PharmacieAdapter extends ArrayAdapter<Medicament> {
	private Context mContext;
	private ArrayList<Medicament> mList;
	private Medicament mMedicament;

	static class PharmacieViewHolder {
		@Bind(R.id.imgPharmacieIcon)
		ImageView icon;
		@Bind(R.id.imgPharmacieIcon2)
		ImageView icon2;
		@Bind(R.id.lblPharmacieTitle)
		TextView title;
		@Bind(R.id.lblPharmacieDescription)
		TextView description;
		@Bind(R.id.lblPharmacieQuantity)
		TextView quantity;

		public PharmacieViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public PharmacieAdapter(Context context, ArrayList<Medicament> list) {
		super(context, R.layout.row_pharmacie, list);
		mContext = context;
		mList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mMedicament = mList.get(position);
		View nView = convertView;
		if(nView == null){
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_pharmacie, parent, false);
			final PharmacieViewHolder nHolder = new PharmacieViewHolder(nView);
			nView.setTag(nHolder);
		}
		PharmacieViewHolder nHolder = (PharmacieViewHolder)nView.getTag();
		nHolder.icon.setImageResource(R.drawable.ng_pill);
		nHolder.title.setText(mMedicament.getTitle());
		nHolder.quantity.setText(mMedicament.getQuantity() + "");
		long nDate = mMedicament.getDate();
		String nUnit = mMedicament.getDateUnit();
		if(nDate > 0){
			nHolder.description.setText(mContext.getResources().getString(R.string.adapter_pharmacie_item_date_to_use) + " " + nDate + " " + nUnit);
			nHolder.icon2.setImageDrawable(null);
		}else{
			nHolder.description.setText(mContext.getResources().getString(R.string.adapter_pharmacie_item_date_peremption) + " " + Math.abs(nDate) + " " + nUnit);
			nHolder.icon2.setImageResource(R.drawable.ng_ball_red);			
		}
		return nView;
	}

}
