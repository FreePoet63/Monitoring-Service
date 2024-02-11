package com.ylab.app.service;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.Audit;
import com.ylab.app.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an auditing mechanism for user actions in the system.
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
public class Audition {
    private final List<String> auditLogs = new ArrayList<>();

    /**
     * Records the specified action performed by the user for auditing purposes.
     *
     * @param user   the user performing the action
     * @param action the action being performed
     * @throws UserValidationException if the user is null or if the action is empty or null
     */
    public void auditAction(User user, Audit action) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (action == null) {
            throw new UserValidationException("Invalid action");
        }
        switch (action) {
            case LOGIN:
                auditLogs.add("User '" + user.getName() + "' logged in on " + LocalDateTime.now() + "\n");
                break;

            case LOGOUT:
                auditLogs.add("User '" + user.getName() + "' logged out on " + LocalDateTime.now() + "\n");
                break;

            case SUBMIT_READING:
                auditLogs.add("User '" + user.getName() + "' submitted a reading on " + LocalDateTime.now() + "\n");
                break;

            case VIEW_CURRENT_READINGS:
                auditLogs.add("User '" + user.getName() + "' viewed current readings on " + LocalDateTime.now() + "\n");
                break;

            case VIEW_READINGS_BY_MONTH:
                auditLogs.add("User '" + user.getName() + "' viewed readings by month on " + LocalDateTime.now() + "\n");
                break;

            case VIEW_READING_HISTORY:
                auditLogs.add("User '" + user.getName() + "' viewed readings history on " + LocalDateTime.now() + "\n");
                break;

            case SESSION_END:
                auditLogs.add("User '" + user.getName() + "' session ended on " + LocalDateTime.now() + "\n");
                break;

            default:
                auditLogs.add("Unknown action: " + action);
                break;
        }
    }

    /**
     * Retrieves the list of audit logs.
     *
     * @return the list of audit logs
     */
    public List<String> getAuditLogs() {
        return auditLogs;
    }
}