package com.ylab.app.service;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomDetailsService class
 *
 * @author HP
 * @since 25.02.2024
 */
@Service
public class CustomDetailsService implements UserDetailsService {
    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new UserDetailsServiceImpl with the specified userDao.
     *
     * @param  jdbcTemplate the jdbcTemplate data access object
     */
    public CustomDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = new UserDaoImpl(jdbcTemplate);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}