package fr.nawrasg.atlantis.interfaces;

/**
 * Created by Nawras on 05/12/2016.
 */

public interface Widget extends Comparable<Widget> {

    public int getOrder();
    public void setOrder(int order);

    @Override
    int compareTo(Widget another);
}
