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
import fr.nawrasg.atlantis.type.Medicament;

public class PharmacieAdapter extends ArrayAdapter<Medicament> {
	private Context mContext;
	private ArrayList<Medicament> mList;
	private Medicament mMedicament;

	static class PharmacieViewHolder {
		public ImageView icon;
		public ImageView icon2;
		public TextView title;
		public TextView description;
		public TextView quantity;
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
			final PharmacieViewHolder nHolder = new PharmacieViewHolder();
			nHolder.title = (TextView)nView.findViewById(R.id.lblPharmacieTitle);
			nHolder.description = (TextView)nView.findViewById(R.id.lblPharmacieDescription);
			nHolder.quantity = (TextView)nView.findViewById(R.id.lblPharmacieQuantity);
			nHolder.icon = (ImageView)nView.findViewById(R.id.imgPharmacieIcon);
			nHolder.icon2 = (ImageView)nView.findViewById(R.id.imgPharmacieIcon2);
			nView.setTag(nHolder);
		}
		PharmacieViewHolder nHolder = (PharmacieViewHolder)nView.getTag();
		nHolder.icon.setImageResource(R.drawable.ng_pill);
		nHolder.title.setText(mMedicament.getTitle());
		nHolder.quantity.setText(mMedicament.getQuantity() + "");
		long nDate = mMedicament.getDate();
		String nUnit = mMedicament.getDateUnit();
		if(nDate > 0){
			nHolder.description.setText("A consommer dans " + nDate + " " + nUnit);
			nHolder.icon2.setImageDrawable(null);
		}else{
			nHolder.description.setText("Périmé depuis " + Math.abs(nDate) + " " + nUnit);
			nHolder.icon2.setImageResource(R.drawable.ng_ball_red);			
		}
		return nView;
	}

}
