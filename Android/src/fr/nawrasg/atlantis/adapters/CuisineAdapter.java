package fr.nawrasg.atlantis.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Produit;

public class CuisineAdapter extends ArrayAdapter<Produit> implements Filterable{
	private Context mContext;
	private ArrayList<Produit> mList;
	private ArrayList<Produit> mOriginList;
	private Produit mProduit;
	private boolean mSeeAll;

	static class CuisineViewHolder {
		public ImageView icon;
		public ImageView icon2;
		public TextView title;
		public TextView description;
		public TextView quantity;
	}

	public CuisineAdapter(Context context, ArrayList<Produit> list) {
		super(context, R.layout.row_cuisine, list);
		mContext = context;
		mOriginList = list;
        mSeeAll = false;
		showAll(false);
		//mList = list;
	}

	private void showAll(boolean all){
		if(all){
			mList = mOriginList;
			mSeeAll = true;
		}else{
			mList = new ArrayList<>();
			for(int i = 0; i < mOriginList.size(); i++){
				Produit nProduit = mOriginList.get(i);
				if(!nProduit.isIgnore()){
					mList.add(nProduit);
				}
			}
			mSeeAll = false;
		}
        notifyDataSetChanged();
	}

	public void toggleShow(){
		if(mSeeAll){
			showAll(false);
		}else{
			showAll(true);
		}
	}

	public boolean isShowAll(){
		return mSeeAll;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public Produit getItem(int position) {
		return mList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mProduit = mList.get(position);
		View nView = convertView;
		if (nView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			nView = inflater.inflate(R.layout.row_cuisine, parent, false);
			final CuisineViewHolder nHolder = new CuisineViewHolder();
			nHolder.icon = (ImageView) nView.findViewById(R.id.imgCuisineIcon);
			nHolder.icon2 = (ImageView) nView.findViewById(R.id.imgCuisineIcon2);
			nHolder.title = (TextView) nView.findViewById(R.id.lblCuisineTitle);
			nHolder.description = (TextView) nView.findViewById(R.id.lblCuisineDescription);
			nHolder.quantity = (TextView) nView.findViewById(R.id.lblCuisineQuantity);
			nView.setTag(nHolder);
		}

		CuisineViewHolder nHolder = (CuisineViewHolder) nView.getTag();
		nHolder.icon.setImageResource(R.drawable.ng_roling);
		nHolder.title.setText(mProduit.getTitre());
		nHolder.quantity.setText(mProduit.getQuantite() + "");
		long nDate = mProduit.getDate();
		String nDateUnit = mProduit.getDateUnit();
		int nStatus = mProduit.getStatus();
		if (mProduit.getPlace().equals("congelateur")) {
			nHolder.description.setText("Congelé depuis " + nDate + " " + nDateUnit);
			nHolder.icon2.setImageResource(R.drawable.ng_ball_blue);
		} else {
			if (nDate < 0) {
				nHolder.description.setText("Périmé depuis " + Math.abs(nDate) + " " + nDateUnit);
				nHolder.icon2.setImageResource(R.drawable.ng_ball_red);
			} else {
				if (nStatus == 1) {
					nHolder.description.setText("Ouvert depuis " + Math.abs(mProduit.getDate2()) + " " + mProduit.getDate2Unit());
					nHolder.icon2.setImageResource(R.drawable.ng_ball_orange);
				} else {
					nHolder.description.setText("A consommer dans " + Math.abs(nDate) + " " + nDateUnit);
					if(mProduit.getPlace().equals("frigidaire")){
						nHolder.icon2.setImageResource(R.drawable.ng_ball_green);												
					}else{
						nHolder.icon2.setImageDrawable(null);						
					}
				}
			}
		}
		return nView;
	}
	
	@Override
	public Filter getFilter() {
		Filter nFilter = new Filter(){

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults nResults = new FilterResults();
				
				ArrayList<Produit> nFilteredList = new ArrayList<Produit>();
				
				if(mOriginList == null){
					mOriginList = new ArrayList<Produit>(mList);
				}
				
				if(constraint == null || constraint.length() == 0){
					nResults.count = mOriginList.size();
					nResults.values = mOriginList;
				}else{
					constraint = constraint.toString().toLowerCase(Locale.FRANCE);
					for(int i = 0; i < mOriginList.size(); i++){
						String nName = mOriginList.get(i).getTitre();
						if(nName.toLowerCase(Locale.FRANCE).startsWith(constraint.toString())){
							nFilteredList.add(mOriginList.get(i));
						}
					}
					nResults.count = nFilteredList.size();
					nResults.values = nFilteredList;
				}
				return nResults;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				mList = (ArrayList<Produit>) results.values;
				notifyDataSetChanged();
			}
			
		};
		return nFilter;
	}

}
