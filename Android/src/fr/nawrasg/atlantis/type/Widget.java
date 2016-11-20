package fr.nawrasg.atlantis.type;

import android.database.Cursor;

import fr.nawrasg.atlantis.other.AtlantisContract;

/**
 * Created by Nawras on 20/11/2016.
 */

public class Widget {
    private int mID, mType, mOrder, mItem;

    public static int WIDGET_SCENARIO = 0;
    public static int WIDGET_LIGHT = 1;
    public static int WIDGET_PLANT = 2;

    public Widget(Cursor cursor) {
        mID = cursor.getInt(cursor.getColumnIndex(AtlantisContract.Widgets.COLUMN_ID));
        mType = cursor.getInt(cursor.getColumnIndex(AtlantisContract.Widgets.COLUMN_TYPE));
        mOrder = cursor.getInt(cursor.getColumnIndex(AtlantisContract.Widgets.COLUMN_ORDER));
        mItem = cursor.getInt(cursor.getColumnIndex(AtlantisContract.Widgets.COLUMN_ITEM));
    }

    public int getID() {
        return mID;
    }

    public int getType() {
        return mType;
    }

    public int getOrder() {
        return mOrder;
    }

    public int getItem() {
        return mItem;
    }
}
