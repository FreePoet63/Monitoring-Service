package com.ylab.app.model;

/**
 * Audit class representing an audit entry.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
public class Audit {
    private Long id;
    private String message;

    /**
     * Instantiates a new Audit with the given message.
     *
     * @param message the message for the audit entry
     */
    public Audit(String message) {
        this.message = message;
    }

    /**
     * Gets the ID of the audit entry.
     *
     * @return the id of the audit entry
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the audit entry.
     *
     * @param id the id to set for the audit entry
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the message of the audit entry.
     *
     * @return the message of the audit entry
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the audit entry.
     *
     * @param message the message to set for the audit entry
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}