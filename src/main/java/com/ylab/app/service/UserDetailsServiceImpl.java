package com.ylab.app.service;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserDetailsServiceImpl class implementing the UserDetailsService for user authentication and authorization.
 *
 * @author razlivinsky
 * @since 14.02.2024
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDaoImpl userDao;

    /**
     * Constructs a new UserDetailsServiceImpl with the specified userDao.
     *
     * @param userDao the user data access object
     */
    public UserDetailsServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
