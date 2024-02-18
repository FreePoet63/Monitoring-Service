package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static com.ylab.app.constants.CreateSchemaSql.INSERT_USER_SCHEMA;
import static com.ylab.app.constants.SqlQueryClass.*;

/**
 * The UserDaoImpl class provides methods for data access related to users in the database. It includes methods for inserting a new user, finding a user by name and password, and getting a list of all users.
 *
 * @author razlivinsky
 * @since 29.01.2024
 */
@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Instantiates a new UserDaoImpl.
     *
     * @param jdbcTemplate the JdbcTemplate for database interaction
     */
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        UserRole role = UserRole.fromString(rs.getString("role"));
        User user = new User(rs.getString("name"), rs.getString("password"), role);
        user.setId(rs.getLong("id"));
        return user;
    };

    /**
     * Inserts a new user into the database.
     *
     * @param user the user object to be inserted
     * @throws DatabaseWriteException if an error occurs during the insertion
     */
    @Override
    public void insertUser(User user) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SCHEMA, new String[] {"id"});
                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole().name());
                return ps;
            }, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Failed to insert user", e);
        }
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a list of all users
     * @throws DatabaseReadException if an error occurs while retrieving the data
     */
    @Override
    public List<User> getAllUsers() {
        try {
            return jdbcTemplate.query(ALL_USERS, userRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve all users", e);
        }
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     * @throws UserValidationException if the user with the given ID is not found
     * @throws DatabaseReadException if an error occurs while retrieving the data
     */
    @Override
    public User findUserById(long id) {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserValidationException("user not found", e);
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve user by id", e);
        }
    }


    /**
     * Finds a user by their login name.
     *
     * @param login the login name of the user to find
     * @return the user with the specified login name
     * @throws UserValidationException if the user with the given login name is not found
     * @throws DatabaseReadException if an error occurs while retrieving the data
     */
    @Override
    public User getUserByLogin(String login) {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_LOGIN, userRowMapper, login);
        } catch (EmptyResultDataAccessException e) {
            throw new UserValidationException("user not found", e);
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve user by login", e);
        }
    }
}
