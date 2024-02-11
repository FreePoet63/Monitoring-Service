package com.ylab.app.model;

/**
 * The Audit enum represents the various audit events related to user actions.
 *
 * @author razlivinsky
 * @since 01.02.2024
 */
public enum Audit {
    LOGIN,
    LOGOUT,
    SUBMIT_READING,
    VIEW_CURRENT_READINGS,
    VIEW_READINGS_BY_MONTH,
    VIEW_READING_HISTORY,
    SESSION_END
}