package fr.nawrasg.atlantis.adapters.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;

public class CuisinePlaceAdapter extends ArrayAdapter<String>{
	private Context mContext;
	private static String[] mList = {"Frigidaire", "Placard", "Congelateur"};
	
	static class CuisinePlaceViewHolder {
		public TextView title;
	}

	public CuisinePlaceAdapter(Context context) {
		super(context, R.layout.row_cuisine_place, mList);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getRow(position, convertView, parent);
	}
	
	private View getRow(int position, View convertView, ViewGroup parent){
		View nView = convertView;
		if(nView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_cuisine_place, parent, false);
			CuisinePlaceViewHolder nHolder = new CuisinePlaceViewHolder();
			nHolder.title = (TextView) nView.findViewById(R.id.lblCuisinePlaceTitle);
			nView.setTag(nHolder);
		}
		CuisinePlaceViewHolder nHolder = (CuisinePlaceViewHolder) nView.getTag();
		nHolder.title.setText(mList[position]);
		switch (position) {
			case 0:
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_fridge, 0, 0, 0);
				break;
			case 1:
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_cabinet, 0, 0, 0);
				break;
			case 2:
				nHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ng_freeze, 0, 0, 0);
				break;
		}
		return nView;
	}

}
