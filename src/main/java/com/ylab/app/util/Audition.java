package com.ylab.app.util;

import com.ylab.app.model.*;

import java.time.*;
import java.util.*;

/**
 * Represents an auditing mechanism for user actions in the system.
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
public class Audition {
    private List<String> auditLogs = new ArrayList<>();

    /**
     * Records the specified action performed by the user for auditing purposes.
     *
     * @param user   the user performing the action
     * @param action the action being performed
     * @throws IllegalArgumentException if the user is null or if the action is empty or null
     */
    public void auditAction(User user, String action) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (action == null || action.isEmpty()) {
            throw new IllegalArgumentException("Invalid action");
        }
        switch (action) {
            case "Login":
                auditLogs.add("User '" + user.getName() + "' logged in on " + LocalDateTime.now() + "\n");
                break;

            case "Logout":
                auditLogs.add("User '" + user.getName() + "' logged out on " + LocalDateTime.now() + "\n");
                break;

            case "Submit Reading":
                auditLogs.add("User '" + user.getName() + "' submitted a reading on " + LocalDateTime.now() + "\n");
                break;

            case "View Current Readings":
                auditLogs.add("User '" + user.getName() + "' viewed current readings on " + LocalDateTime.now() + "\n");
                break;

            case "View Readings by Month":
                auditLogs.add("User '" + user.getName() + "' viewed readings by month on " + LocalDateTime.now() + "\n");
                break;

            case "View Readings History":
                auditLogs.add("User '" + user.getName() + "' viewed readings history on " + LocalDateTime.now() + "\n");
                break;

            case "Session End":
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