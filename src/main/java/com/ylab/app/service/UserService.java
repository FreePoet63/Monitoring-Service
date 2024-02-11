package com.ylab.app.service;

import com.ylab.app.model.*;
import com.ylab.app.model.dto.UserDto;

import java.sql.SQLException;
import java.util.*;

/**
 * An interface for managing users in the system.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface UserService {
    /**
     * Registers a new user with the given name, password, and role.
     *  @param name     the name of the user
     * @param password the password for the user
     * @return
     */
    UserDto registerUser(String name, String password);

    /**
     * Logs in a user with the name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     */
    UserDto loginUser(String name, String password);

    /**
     * Checks if the user has the admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     */
    boolean hasRoleAdmin(User user);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of all users
     * @throws SQLException the sql exception
     */
    List<UserDto> getAllUsers() throws SQLException;

    /**
     * Gets user by id.
     *
     * @param userId the user id
     * @return the user by id
     */
    UserDto getUserById(long userId);

    /**
     * Gets user by login.
     *
     * @param login the login
     * @return the user by login
     */
    UserDto getUserByLogin(String login);
}

