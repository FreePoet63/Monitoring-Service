package com.ylab.app.dbService.dao;

import com.ylab.app.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * The UserDao interface provides methods for data access related to users in the database.
 * It includes methods for inserting a new user, finding a user by name and password, and getting a list of all users.
 *
 * @author razlivinsky
 * @since 03.02.2024
 */
public interface UserDao {
    /**
     * Inserts the provided user into the database.
     *
     * @param user the user object to be inserted
     * @throws SQLException if an error occurs while interacting with the database
     */
    public void insertUser(User user) throws SQLException;

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a list of all users in the database
     * @throws SQLException if an error occurs while interacting with the database
     */
    public List<User> getAllUsers() throws SQLException;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     */
    public User findUserById(long id);

    /**
     * Retrieves a user by their login name.
     *
     * @param login the login name of the user to find
     * @return the user with the specified login name
     */
    public User getUserByLogin(String login);
}
