package com.ylab.app.service.impl;

import com.ylab.app.aspect.LogExecution;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.Session;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a service for managing users in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
@LogExecution
public class UserServiceImpl implements UserService {
    private UserDaoImpl userDao;

    /**
     * Instantiates a new user service with an empty user map.
     */
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    /**
     * Registers a new user with the given name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @throws UserValidationException  if the name, password, or role is invalid
     * @return
     */
    public UserDto registerUser(String name, String password) {
        validateFromUserNameAndPassword(name, password);
        User user = new User(name, password, UserRole.USER);
        try {
            userDao.insertUser(user);
        } catch (SQLException e) {
            throw new UserValidationException("Problem registration " + e.getMessage());
        }
        UserMapper userMapper = UserMapper.INSTANCE;
        return userMapper.userToUserDto(user);
    }

    /**
     * Logs in a user with the provided name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws UserValidationException  if the name or password is invalid
     */
    public UserDto loginUser(String name, String password) {
        validateFromUserNameAndPassword(name, password);
        try {
            User user = userDao.findUserByNameAndPassword(name, password);
            if (user == null) {
                throw new UserValidationException("Invalid credentials");
            }
            Session session = Session.getInstance();
            session.setUser(user);
            UserMapper userMapper = UserMapper.INSTANCE;
            return userMapper.userToUserDto(user);
        } catch (SQLException e) {
            throw new UserValidationException("An error occurred while logging in. Please try again. " + e.getMessage());
        }
    }

    /**
     * Checks if the user has admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     * @throws UserValidationException if the user is null
     */
    public boolean hasRoleAdmin(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (user.getRole().equals(UserRole.ADMIN)) {
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
    public List<UserDto> getAllUsers() {
        try {
            List<User> users = new ArrayList<>(userDao.getAllUsers());
            UserMapper userMapper = UserMapper.INSTANCE;

            return users.stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserValidationException("An error occurred while retrieving users. Please try again.");
        }
    }

    /**
     * Validates the username and password for any potential issues.
     *
     * @param name     the username to be validated
     * @param password the password to be validated
     * @throws UserValidationException if the username or password is invalid
     */
    private void validateFromUserNameAndPassword(String name, String password) {
        if (name == null || name.isEmpty()) {
            throw new UserValidationException("Invalid credentials");
        }
        if (password == null || password.isEmpty()) {
            throw new UserValidationException("Invalid credentials");
        }
    }

    /**
     * Finds a user in the system by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID, or null if no such user is found
     * @throws UserValidationException if the user ID is invalid
     */
    public UserDto getUserById(long id) {
        try {
            User user = userDao.findUserById(id);
            UserMapper userMapper = UserMapper.INSTANCE;
            return userMapper.userToUserDto(user);
        } catch (SQLException e) {
            throw new UserValidationException("An error occurred while finding user by ID. " + e.getMessage());
        }
    }

    /**
     * Finds a user in the system by their ID.
     *
     * @param login the ID of the user to find
     * @return the user with the specified ID, or null if no such user is found
     * @throws UserValidationException if the user login is invalid
     */
    public UserDto getUserByLogin(String login) {
        try {
            User user = userDao.getUserByLogin(login);
            UserMapper userMapper = UserMapper.INSTANCE;
            return userMapper.userToUserDto(user);
        } catch (SQLException e) {
            throw new UserValidationException("An error occurred while finding user by ID. " + e.getMessage());
        }
    }
}
