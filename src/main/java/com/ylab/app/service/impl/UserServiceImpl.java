package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a service for managing users in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User service.
     *
     * @param userDao         the user data access object
     * @param userMapper      the user mapper
     * @param passwordEncoder the password encoder
     */
    public UserServiceImpl(UserDao userDao, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user the user to be registered
     * @return the data transfer object for the registered user
     * @throws SQLException if an SQL exception occurs
     */
    @Override
    public UserDto registerUser(User user) throws SQLException {
        validateFromUserNameAndPassword(user);
        user.setName(user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ADMIN);
        userDao.insertUser(user);
        return userMapper.userToUserDto(user);
    }

    /**
     * Checks if the user has the "admin" role.
     *
     * @param user the user to be checked
     * @return true if the user has the "admin" role, otherwise false
     */
    @Override
    public boolean hasRoleAdmin(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        return user.getRole().equals(UserRole.ADMIN);
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of user data transfer objects
     * @throws SQLException if an SQL exception occurs
     */
    @Override
    public List<UserDto> getAllUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the data transfer object for the retrieved user
     */
    @Override
    public UserDto getUserById(long id) {
        User user = userDao.findUserById(id);
        return userMapper.userToUserDto(user);
    }

    /**
     * Retrieves a user by their login credentials.
     *
     * @param login the login credentials of the user to retrieve
     * @return the data transfer object for the retrieved user
     * @throws UserValidationException if the credentials are invalid
     */
    @Override
    public UserDto getUserByLogin(String login) {
        User user = userDao.getUserByLogin(login);
        try {
            if (user == null) {
                throw new UserValidationException("Invalid credentials");
            }
            return userMapper.userToUserDto(user);
        } catch (DataAccessException e) {
            throw new UserValidationException("Problem registration " + e.getMessage());
        }
    }

    private void validateFromUserNameAndPassword(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserValidationException("Invalid credentials");
        }
    }
}
