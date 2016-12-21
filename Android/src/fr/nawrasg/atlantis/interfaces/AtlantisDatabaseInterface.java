package fr.nawrasg.atlantis.interfaces;

/**
 * Created by Nawras on 23/06/2016.
 */

public interface AtlantisDatabaseInterface {
    String LIGHTS_TABLE_NAME = "at_lights";
    String ROOMS_TABLE_NAME = "at_room";
    String EAN_TABLE_NAME = "at_ean";
    String SCENARIOS_TABLE_NAME = "at_scenarios";
    String PLANTS_TABLE_NAME = "at_plants";
    String DEVICES_TABLE_NAME = "at_devices";
    String SENSORS_TABLE_NAME = "at_sensors";
    String SENSORS_DEVICES_TABLE_NAME = "at_sensors_devices";
    String TABLE_NAME_WIDGETS = "at_widgets";

    String EAN_TABLE_CREATE = "CREATE TABLE at_ean (" +
            "ean TEXT UNIQUE, \n" +
            "nom TEXT" +
            ")";

    String COURSES_TABLE_CREATE = "CREATE TABLE at_courses (" +
            "id INTEGER UNIQUE, \n" +
            "name TEXT NOT NULL, \n" +
            "quantity INTEGER, \n" +
            "lastmodified INTEGER" +
            ")";

    String SCENARIOS_TABLE_CREATE = "CREATE TABLE at_scenarios (" +
            "file TEXT NOT NULL" +
            ")";

    String LIGHTS_TABLE_CREATE = "CREATE TABLE at_lights (" +
            "id TEXT UNIQUE, \n" +
            "name TEXT, \n" +
            "protocol TEXT, \n" +
            "type TEXT, \n" +
            "ip TEXT, \n" +
            "room TEXT, \n" +
            "uid TEXT" +
            ")";

    String ROOMS_TABLE_CREATE = "CREATE TABLE at_room (" +
            "id INTEGER NOT NULL, \n" +
            "room TEXT" +
            ")";

    String PLANTS_TABLE_CREATE = "CREATE TABLE at_plants (" +
            "id INTEGER UNIQUE NOT NULL, \n" +
            "sensor TEXT, \n" +
            "title TEXT, \n" +
            "picture TEXT, \n" +
            "color TEXT, \n" +
            "room INTEGER, \n" +
            "timestamp TEXT" +
            ")";

    String DEVICES_TABLE_CREATE = "CREATE TABLE at_devices (" +
            "id INTEGER UNIQUE NOT NULL, \n" +
            "nom TEXT, \n" +
            "ip TEXT, \n" +
            "mac TEXT, \n" +
            "dns TEXT, \n" +
            "port INTEGER, \n" +
            "type TEXT, \n" +
            "connexion TEXT, \n" +
            "note TEXT, \n" +
            "username TEXT" +
            ")";

    String SENSORS_TABLE_CREATE = "CREATE TABLE at_sensors (" +
            "id INTEGER UNIQUE NOT NULL, \n" +
            "sensor TEXT, \n" +
            "device TEXT, \n" +
            "protocol TEXT, \n" +
            "type TEXT, \n" +
            "unit TEXT, \n" +
            "history INTEGER, \n" +
            "date TEXT, \n" +
            "time TEXT, \n" +
            "ignore INTEGER" +
            ")";

    String SENSORS_DEVICES_TABLE_CREATE = "CREATE TABLE at_sensors_devices (" +
            "id INTEGER UNIQUE NOT NULL, \n" +
            "device TEXT, \n" +
            "alias TEXT, \n" +
            "room INTEGER" +
            ")";

    String TABLE_CREATE_WIDGETS = "CREATE TABLE " + TABLE_NAME_WIDGETS + " (" +
            "_id INTEGER PRIMARY KEY, \n" +
            "type INTEGER, \n" +
            "item TEXT, \n" +
            "rang INTEGER NOT NULL DEFAULT 0)";

}
