package fr.nawrasg.atlantis.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Produit;

/**
 * Created by Nawras on 16/11/2016.
 */

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.KitchenViewHolder> {
    private ArrayList<Produit> mList;

    public KitchenAdapter(ArrayList<Produit> list) {
        mList = list;
    }

    @Override
    public KitchenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_kitchen, parent, false);
        return new KitchenViewHolder(nView);
    }

    @Override
    public void onBindViewHolder(KitchenViewHolder holder, int position) {
        Produit nProduit = mList.get(position);
        holder.cv.setTag(R.id.tag_object, nProduit);
        holder.cv.setTag(R.id.tag_adapter, this);
        holder.label.setText(nProduit.getTitre());
        holder.quantity.setText(nProduit.getQuantite() + "");
        holder.icon.setImageResource(R.drawable.ng_roling);
        long nDate = nProduit.getDate();
        String nDateUnit = nProduit.getDateUnit();
        int nStatus = nProduit.getStatus();
        if (nProduit.getPlace().equals(Produit.PLACE_FREEZER)) {
            holder.description.setText(App.getContext().getResources().getString(R.string.adapter_cuisine_item_date_frozen) + " " + nDate + " " + nDateUnit);
            holder.place.setImageResource(R.drawable.ng_ball_blue);
        } else {
            if (nStatus == 1) {
                holder.description.setText(App.getContext().getResources().getString(R.string.adapter_cuisine_item_date_open) + " " + Math.abs(nProduit.getDate2()) + " " + nProduit.getDate2Unit());
                holder.place.setImageResource(R.drawable.ng_ball_orange);
            } else {
                holder.description.setText(App.getContext().getResources().getString(R.string.adapter_cuisine_item_date_to_use) + " " + Math.abs(nDate) + " " + nDateUnit);
                if (nProduit.getPlace().equals(Produit.PLACE_FRIDGE)) {
                    holder.place.setImageResource(R.drawable.ng_ball_green);
                } else {
                    holder.place.setImageDrawable(null);
                }
            }
        }
        if (nDate < 0) {
            holder.expiry.setBackgroundColor(Color.RED);
            holder.description.setText(App.getContext().getResources().getString(R.string.adapter_cuisine_item_date_peremption) + " " + Math.abs(nDate) + " " + nDateUnit);
        }else{
            holder.expiry.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class KitchenViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        @Bind(R.id.lblCuisineTitle)
        TextView label;
        @Bind(R.id.lblCuisineDescription)
        TextView description;
        @Bind(R.id.lblCuisineQuantity)
        TextView quantity;
        @Bind(R.id.imgCuisineIcon)
        ImageView icon;
        @Bind(R.id.imgCuisinePlace)
        ImageView place;
        @Bind(R.id.imgCuisineExpiry)
        View expiry;
        @Bind(R.id.cvKitchen)
        CardView cv;

        public KitchenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
