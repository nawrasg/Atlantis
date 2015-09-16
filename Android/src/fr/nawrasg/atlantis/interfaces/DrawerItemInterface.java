package fr.nawrasg.atlantis.interfaces;

public interface DrawerItemInterface {
	int getId();
    String getLabel();
    int getType();
    boolean isEnabled();
    boolean updateActionBarTitle();
}
