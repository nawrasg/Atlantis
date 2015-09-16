package fr.nawrasg.atlantis.type;

import fr.nawrasg.atlantis.interfaces.DrawerItemInterface;

public class DrawerSection implements DrawerItemInterface{
	public static final int SECTION_TYPE = 0;
    private int id;
    private String label;
    
    public static DrawerSection create(int id, String label) {
    	DrawerSection nDS = new DrawerSection();
    	nDS.setLabel(label);
    	return nDS;
	}
    
	private void setLabel(String label2) {
		label = label2;
	}
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int getType() {
		return SECTION_TYPE;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateActionBarTitle() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setId(int id) {
        this.id = id;
    }
}
