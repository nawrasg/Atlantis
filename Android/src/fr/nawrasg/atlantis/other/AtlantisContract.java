package fr.nawrasg.atlantis.other;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nawras on 23/06/2016.
 */

public final class AtlantisContract {

    public static final String AUTHORITY = "fr.nawrasg.atlantis";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String SELECTION_ID_BASED = BaseColumns._ID
            + " = ? ";

    public static final class Ean implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "ean");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.ean";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.ean";
        public static final String[] PROJECTION_ALL = {"ean", "nom"};
    }

    public static final class Courses implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "courses");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.courses";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.courses";
    }

    public static final class Scenarios implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "scenarios");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.scenarios";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.scenarios";
    }

    public static final class Lights implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "lights");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.lights";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.lights";
    }

    public static final class Rooms implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "rooms");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.rooms";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.rooms";
    }

    public static final class Plants implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "plants");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.plants";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.plants";
    }

    public static final class Devices implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "devices");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.devices";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.devices";
    }

    public static final class Sensors implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "sensors");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.sensors";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.sensors";
    }

    public static final class SensorsDevices implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AtlantisContract.CONTENT_URI, "sensors_devices");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.sensors_devices";
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.nawrasg.atlantis.sensors_devices";
    }
}
