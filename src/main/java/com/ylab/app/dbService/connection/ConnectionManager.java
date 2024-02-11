package com.ylab.app.dbService.connection;

import com.ylab.app.exception.dbException.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.ylab.app.util.DataReaderDatabase.*;

/**
 * The ConnectionManager class provides methods for creating and managing database connections.
 * It contains a static method for obtaining a connection to the database.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class ConnectionManager {
    private Connection connection;
    /**
     * Gets a Connection object for the database.
     *
     * @return a Connection object to interact with the database
     * @throws DatabaseConnectionException if an error occurs while attempting to connect to the database
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(getDatabaseData(URL) + getDatabaseData(DATABASE),
                    getDatabaseData(USER), getDatabaseData(PASSWORD));
        } catch (SQLException e) {
            throw new DatabaseConnectionException("invalid connection");
        }
        return connection;
    }

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


