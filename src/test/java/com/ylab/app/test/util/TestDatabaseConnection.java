package com.ylab.app.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TestDatabaseConnection class
 *
 * @author HP
 * @since 11.02.2024
 */
public class TestDatabaseConnection {
    private Connection connection;
    /**
     * Sets the connection database who is logged in
     *
     * @param url a string containing the url of the database
     *
     * @param user a string containing the user of the database
     *
     * @param password a string containing the password of the database
     */
    public void setConnection(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }
}