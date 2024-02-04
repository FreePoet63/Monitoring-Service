package com.ylab.app.service.impl;

import com.ylab.app.model.*;
import com.ylab.app.service.*;

import java.util.*;

/**
 * Represents a service for managing users in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class UserServiceImpl implements UserService {
    private Map<String, User> users;

    /**
     * Instantiates a new user service with an empty user map.
     */
    public UserServiceImpl() {
        this.users = new HashMap<>();
    }

    /**
     * Registers a new user with the given name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user ("user" or "admin")
     * @throws IllegalArgumentException  if the name, password, or role is invalid
     * @throws IllegalStateException      if the user already exists
     */
    public void registerUser(String name, String password, String role) {
        if (name == null || name.isEmpty() || name.contains(" ")) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (password == null || password.isEmpty() || password.contains(" ")) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (role == null || !(role.equals("user") || role.equals("admin"))) {
            throw new IllegalArgumentException("Invalid role");
        }
        if (users.containsKey(name)) {
            throw new IllegalStateException("User already exists");
        }
        User user = new User(name, password, role);
        users.put(name, user);
    }

    /**
     * Logs in a user with the provided name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws IllegalArgumentException  if the name or password is invalid
     * @throws IllegalStateException      if the user does not exist or the password is wrong
     */
    public User loginUser(String name, String password) {
        if (name == null || name.isEmpty() || name.contains(" ")) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (password == null || password.isEmpty() || password.contains(" ")) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!users.containsKey(name)) {
            throw new IllegalStateException("User does not exist");
        }
        User user = users.get(name);
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Wrong password");
        }
        return user;
    }

    /**
     * Checks if the user has admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     * @throws IllegalArgumentException if the user is null
     */
    public boolean checkRole(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (user.getRole().equals("admin")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
