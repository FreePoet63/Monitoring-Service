package com.ylab.app.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a user in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private UserRole role;

    public User() {}

    /**
     * Instantiates a new User with the specified name, password, and role.
     *
     * @param username     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets id of the user.
     *
     * @return the id of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id of the user
     *
     * @param id the id of the user to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
     public String getUsername() {
        return username;
    }

    /**
     * Sets the name of the user.
     *
     * @param username the name of the user to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Returns the collection of authorities granted to the user.
     *
     * @return the authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns whether the user account is non-expired.
     *
     * @return always returns true, indicating that the user account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns whether the user account is non-locked.
     *
     * @return always returns true, indicating that the user account is non-locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns whether the user credentials are non-expired.
     *
     * @return always returns true, indicating that the user credentials are non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user account is enabled.
     *
     * @return always returns true, indicating that the user account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password of the user to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role of the user to be set
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Generates a string representation of the user.
     *
     * @return a string with the user details
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}