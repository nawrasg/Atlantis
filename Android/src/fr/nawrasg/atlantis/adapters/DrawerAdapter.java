package fr.nawrasg.atlantis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.interfaces.DrawerItemInterface;
import fr.nawrasg.atlantis.type.DrawerItem;
import fr.nawrasg.atlantis.type.DrawerSection;


public class DrawerAdapter extends ArrayAdapter<DrawerItemInterface>{
	private LayoutInflater inflater;
	
	public DrawerAdapter(Context context, int textViewResourceId, DrawerItemInterface[] objects){
		super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null ;
        DrawerItemInterface menuItem = this.getItem(position);
        if ( menuItem.getType() == DrawerItem.ITEM_TYPE ) {
            view = getItemView(convertView, parent, menuItem );
        }
        else {
            view = getSectionView(convertView, parent, menuItem);
        }
        return view ;
    }
	
	public View getItemView( View convertView, ViewGroup parentView, DrawerItemInterface navDrawerItem ) {
        
        DrawerItem menuItem = (DrawerItem) navDrawerItem ;
        DrawerItemHolder navMenuItemHolder = null;
        
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.layout_drawer_item, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.itemDrawer_Item);
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.itemDrawer_Icon);

            navMenuItemHolder = new DrawerItemHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (DrawerItemHolder) convertView.getTag();
        }
                    
        navMenuItemHolder.labelView.setText(menuItem.getLabel());
        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
        
        return convertView ;
    }
	
	public View getSectionView(View convertView, ViewGroup parentView,
            DrawerItemInterface navDrawerItem) {
        
        DrawerSection menuSection = (DrawerSection) navDrawerItem ;
        DrawerSectionHolder navMenuItemHolder = null;
        
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.layout_drawer_section, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.itemDrawer_Section );

            navMenuItemHolder = new DrawerSectionHolder();
            navMenuItemHolder.labelView = labelView ;
            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (DrawerSectionHolder) convertView.getTag();
        }
                    
        navMenuItemHolder.labelView.setText(menuSection.getLabel());
        
        return convertView ;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }
    
    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }
    
    
    private static class DrawerItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }
    
    private class DrawerSectionHolder {
        private TextView labelView;
    }
}
