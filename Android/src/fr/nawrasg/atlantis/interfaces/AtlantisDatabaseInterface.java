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
            "id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "name TEXT NOT NULL, \n" +
            "quantity INTEGER, \n" +
            "lastmodified INTEGER" +
            ")";

    String SCENARIOS_TABLE_CREATE = "CREATE TABLE at_scenarios (" +
            "file TEXT NOT NULL" +
            ")";
}
