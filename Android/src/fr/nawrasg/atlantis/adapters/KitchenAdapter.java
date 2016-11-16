package fr.nawrasg.atlantis.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nawras on 16/11/2016.
 */

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.KitchenViewHolder>{

    @Override
    public KitchenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(KitchenViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class KitchenViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public KitchenViewHolder(View itemView) {
            super(itemView);
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
