package com.ylab.app.model.dto;

/**
 * UserDto class
 *
 * @author HP
 * @since 07.02.2024
 */
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String role;

    /**
     * Instantiates a new User dto.
     */
    public UserDto() {}

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name=" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
