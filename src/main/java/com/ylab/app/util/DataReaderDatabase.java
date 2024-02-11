package com.ylab.app.util;

import java.util.*;

/**
 * ReaderDatabase class
 * DataReaderDatabase is a utility class responsible for fetching and retrieving various database configuration data.
 * <p>
 * This class defines several constants that represent keys used to retrieve particular pieces of configuration data
 * from different resource bundles: "app", "liquibase", and "test".
 * <p>
 * Bundles are loaded upon class initialization. Methods are provided to fetch data from
 * each individual resource bundle using specific keys.
 *
 * @author razlivinsky
 * @since 19.10.2023
 */
public class DataReaderDatabase {
    public static final String URL = "url";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DATABASE = "database";
    public static final String DEFAULT_SCHEMA_NAME = "liquibase.defaultSchemaName";

    private static ResourceBundle resourceBundleApp = ResourceBundle.getBundle("app");
    private static ResourceBundle resourceBundleLiquibase = ResourceBundle.getBundle("liquibase");

    /**
     * Retrieves data from the application's database using the provided key.
     *
     * @param key the key for retrieving the data
     * @return the retrieved string data
     */
    public static String getDatabaseData(String key){
        return resourceBundleApp.getString(key);
    }

    /**
     * Retrieves the Liquibase schema data using the provided key.
     *
     * @param key the key for retrieving the schema data
     * @return the retrieved Liquibase schema data as a string
     */
    public static String getLiquibaseSchema(String key) {
        return resourceBundleLiquibase.getString(key);
    }
}