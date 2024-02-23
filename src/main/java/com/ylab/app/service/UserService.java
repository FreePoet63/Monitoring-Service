package com.ylab.app.service;

import com.ylab.app.model.User;
import com.ylab.app.model.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

/**
 * An interface for managing users in the system.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface UserService {
    /**
     * Registers a new user with the given name, password, and role.
     *
     * @param user the user object to be registered
     * @return the UserDto object representing the registered user
     * @throws SQLException if an SQL exception occurs during the user registration process
     */
    UserDto registerUser(User user) throws SQLException;

    /**
     * Checks if the user has the admin role.
     *
     * @param user the user to check
     * @return true if the user has the admin role, false otherwise
     */
    boolean hasRoleAdmin(User user);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of all users as a collection of UserDto objects
     * @throws SQLException if an SQL exception occurs during the retrieval process
     */
    List<UserDto> getAllUsers() throws SQLException;

    /**
     * Gets a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDto object representing the user with the specified ID
     */
    UserDto getUserById(long userId);

    /**
     * Gets a user by their login name.
     *
     * @param login the login name of the user to retrieve
     * @return the UserDto object representing the user with the specified login name
     */
    UserDto getUserByLogin(String login);
}
