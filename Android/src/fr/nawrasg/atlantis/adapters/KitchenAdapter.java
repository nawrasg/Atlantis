package fr.nawrasg.atlantis.adapters;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Produit;

/**
 * Created by Nawras on 16/11/2016.
 */

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.KitchenViewHolder> implements Filterable {
    private ArrayList<Produit> mList;
    private ArrayList<Produit> mOriginalList;
    private Handler mHandler;

    public KitchenAdapter(ArrayList<Produit> list) {
        mList = list;
        mOriginalList = list;
        mHandler = new Handler(Looper.getMainLooper());
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
            if (nProduit.isIgnore()) {
                holder.expiry.setBackgroundColor(Color.BLACK);
            } else {
                holder.expiry.setBackgroundColor(Color.RED);
            }
            holder.description.setText(App.getContext().getResources().getString(R.string.adapter_cuisine_item_date_peremption) + " " + Math.abs(nDate) + " " + nDateUnit);
        } else {
            holder.expiry.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(final Produit produit) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (produit != null) {
                    mList.remove(produit);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public Filter getFilter() {
        Filter nFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults nResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    mList = mOriginalList;
                } else {
                    mList = new ArrayList<>();
                    constraint = constraint.toString().toLowerCase(Locale.FRANCE);
                    for (int i = 0; i < mOriginalList.size(); i++) {
                        Produit nProduit = mOriginalList.get(i);
                        if (nProduit.getTitre().toLowerCase(Locale.FRANCE).startsWith(constraint.toString())) {
                            mList.add(nProduit);
                        }
                    }
                }
                nResults.count = mList.size();
                nResults.values = mList;
                return nResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
        return nFilter;
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
            Produit nProduit = (Produit) cv.getTag(R.id.tag_object);
            switch (item.getItemId()) {
                case R.string.menu_increment:
                    modifyItem(nProduit, '+');
                    return true;
                case R.string.menu_decrement:
                    modifyItem(nProduit, '-');
                    return true;
                case R.string.menu_context_cuisine_open_title:
                    modifyItem(nProduit, ',');
                    return true;
                case R.string.menu_context_cuisine_avoid_title:
                    modifyItem(nProduit, '.');
                    return true;
                case R.string.menu_delete:
                    deleteItem(nProduit);
                    return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Produit nProduit = (Produit) cv.getTag(R.id.tag_object);
            MenuItem nIncrementItem = menu.add(0, R.string.menu_increment, 0, R.string.menu_increment);
            nIncrementItem.setOnMenuItemClickListener(this);
            if (nProduit.getQuantite() > 1) {
                MenuItem nDecrementItem = menu.add(0, R.string.menu_decrement, 1, R.string.menu_decrement);
                nDecrementItem.setOnMenuItemClickListener(this);
            }
            if (nProduit.getStatus() != 1 && !nProduit.getPlace().equals(Produit.PLACE_FREEZER)) {
                MenuItem nOpenItem = menu.add(0, R.string.menu_context_cuisine_open_title, 2, R.string.menu_context_cuisine_open_title);
                nOpenItem.setOnMenuItemClickListener(this);
            }
            if (!nProduit.isIgnore()) {
                MenuItem nIgnoreItem = menu.add(0, R.string.menu_context_cuisine_avoid_title, 3, R.string.menu_context_cuisine_avoid_title);
                nIgnoreItem.setOnMenuItemClickListener(this);
            }
            MenuItem nDeleteItem = menu.add(0, R.string.menu_delete, 4, R.string.menu_delete);
            nDeleteItem.setOnMenuItemClickListener(this);
        }

        private void modifyItem(final Produit produit, final char mode) {
            String nURL = App.getUri(App.getContext(), App.CUISINE);
            switch (mode) {
                case ',':
                    nURL += "&open=" + produit.getID();
                    break;
                case '+':
                    nURL += "&id=" + produit.getID() + "&quantite=" + (produit.getQuantite() + 1);
                    break;
                case '-':
                    nURL += "&id=" + produit.getID() + "&quantite=" + (produit.getQuantite() - 1);
                    break;
                case '.':
                    nURL += "&ignore=" + produit.getID();
                    break;
            }
            Request nRequest = new Request.Builder()
                    .url(nURL)
                    .put(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), ""))
                    .build();
            App.httpClient.newCall(nRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.code() == 202) {
                        switch (mode) {
                            case ',':
                                produit.open();
                                break;
                            case '+':
                                produit.increment();
                                break;
                            case '-':
                                produit.decrement();
                                break;
                            case '.':
                                produit.ignore();
                                break;
                        }
                        ((KitchenAdapter) cv.getTag(R.id.tag_adapter)).update(null);
                    }
                }
            });
        }

        private void deleteItem(final Produit produit) {
            String nURL = App.getUri(App.getContext(), App.CUISINE) + "&id=" + produit.getID();
            Request nRequest = new Request.Builder()
                    .url(nURL)
                    .delete()
                    .build();
            App.httpClient.newCall(nRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d("Nawras", response.code() + "");
                    if (response.code() == 202) {
                        ((KitchenAdapter) cv.getTag(R.id.tag_adapter)).update(produit);
                    }
                }
            });
        }
    }
}
