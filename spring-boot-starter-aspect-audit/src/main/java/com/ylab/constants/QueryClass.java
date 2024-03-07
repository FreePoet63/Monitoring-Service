package com.ylab.constants;

/**
 * QueryClass class provides predefined SQL queries for audit operations.
 *
 * This class encapsulates SQL queries related to the 'audit' table.
 *
 * @author razlivinsky
 * @since 06.03.2024
 */
public class QueryClass {
    /**
     * SQL query to retrieve all audit records from the 'audit' table.
     */
    public static final String GET_AUDIT = "SELECT * FROM my_schema.audit";
}