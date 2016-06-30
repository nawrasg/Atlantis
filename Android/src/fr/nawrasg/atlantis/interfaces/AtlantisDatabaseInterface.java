package fr.nawrasg.atlantis.interfaces;

/**
 * Created by Nawras on 23/06/2016.
 */

public interface AtlantisDatabaseInterface {
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

    String LIGHTS_TABLE_CREATE = "CREATE TABLE at_lights ("+
            "id TEXT UNIQUE, \n" +
            "name TEXT, \n" +
            "protocol TEXT, \n" +
            "type TEXT, \n" +
            "ip TEXT, \n" +
            "room TEXT, \n" +
            "uid TEXT" +
            ")";

    String LIGHTS_TABLE_NAME = "at_lights";
}
