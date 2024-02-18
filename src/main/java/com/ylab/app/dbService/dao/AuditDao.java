package com.ylab.app.dbService.dao;

import com.ylab.app.model.Audit;

import java.util.List;

/**
 * The AuditDao interface provides methods for sending and retrieving audit messages.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
public interface AuditDao {
    /**
     * Sends an audit message to the data store.
     *
     * @param audit the audit message to be sent
     */
    public void sendMessage(Audit audit);

    /**
     * Retrieves a list of all audit messages from the data store.
     *
     * @return a list of all audit messages
     */
    public List<Audit> getMessage();
}