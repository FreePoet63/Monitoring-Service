package com.ylab.app.model;

/**
 * Represents a user in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class User {
    private Long id;
    private String name;
    private String password;
    private UserRole role;

    public User() {}

    /**
     * Instantiates a new User with the specified name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public User(String name, String password, UserRole role) {
        this.name = name;
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
    public String getName() {

        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name of the user to be set
     */
    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}