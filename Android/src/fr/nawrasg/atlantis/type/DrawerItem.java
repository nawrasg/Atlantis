package fr.nawrasg.atlantis.type;

import android.content.Context;
import fr.nawrasg.atlantis.interfaces.DrawerItemInterface;

public class DrawerItem implements DrawerItemInterface {
	public static final int ITEM_TYPE = 1 ;

    private int id ;
    private String label ;  
    private int icon ;
    private boolean updateActionBarTitle ;
    
    public static DrawerItem create( int id, String label, String icon, boolean updateActionBarTitle, Context context ) {
        DrawerItem item = new DrawerItem();
        item.setId(id);
        item.setLabel(label);
        item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }

	public static DrawerItem create( int id, int label, String icon, boolean updateActionBarTitle, Context context ) {
		DrawerItem item = new DrawerItem();
		item.setId(id);
		item.setLabel(context.getResources().getString(label));
		item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
		item.setUpdateActionBarTitle(updateActionBarTitle);
		return item;
	}

	private void setLabel(String label2) {
		this.label = label2;
	}
	private void setId(int id2) {
		this.id = id2;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int getType() {
		return ITEM_TYPE;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean updateActionBarTitle() {
		// TODO Auto-generated method stub
		return this.updateActionBarTitle;
	}
	
	public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    
    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }
}
