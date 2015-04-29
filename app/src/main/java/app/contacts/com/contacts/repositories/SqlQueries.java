package app.contacts.com.contacts.repositories;

public class SqlQueries {

    public static final String CREATE_SETTINGS_TABLE = "CREATE TABLE Settings(" +
                                                       "sort_by TEXT DEFAULT 0" +
                                                       "order TEXT DEFAULT 0)";

    public static final String GET_SETTINGS = "SELECT * FROM Settings";


}
