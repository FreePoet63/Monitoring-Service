package com.ylab.app.model;

import com.ylab.app.exception.userException.UserValidationException;
import org.springframework.security.core.GrantedAuthority;

/**
 * The UserRole enum represents the various roles that users can have within the system.
 * This enum implements the GrantedAuthority interface to represent user roles as authorities.
 *
 * @author razlivinsky
 * @since 01.02.2024
 */
public enum UserRole implements GrantedAuthority {
    ADMIN,
    USER;

    /**
     * Returns the UserRole enum value from the given role string.
     *
     * @param roleStr the role string
     * @return the corresponding UserRole enum value
     * @throws UserValidationException if the role string does not match any UserRole
     */
    public static UserRole fromString(String roleStr) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new UserValidationException("Unknown role: " + roleStr);
    }

    /**
     * Returns the authority of the user role.
     *
     * @return the authority of the user role
     */
    @Override
    public String getAuthority() {
        return name();
    }
}