package com.ylab.app.test.util;

import java.util.ResourceBundle;

/**
 * TestDataReader class
 *
 * @author HP
 * @since 04.02.2024
 */
public class TestDataReader {
    public static final String TEST_DATABASE = "database";
    public static final String TEST_USER = "user";
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_DATABASE_VERSION = "version";

    private static ResourceBundle resourceBundleTestContainers = ResourceBundle.getBundle("test");

    /**
     * Gets test data database.
     *
     * @param key the key
     * @return the test data database
     */
    public static String getTestDataDatabase(String key) {
        return resourceBundleTestContainers.getString(key);
    }

    public static void main(String[] args) {
        System.out.println(getTestDataDatabase(TEST_DATABASE));
    }
}