package com.ylab.constants;

/**
 * SchemaSql class provides predefined SQL statements for database operations related to the audit schema.
 *
 * This class encapsulates SQL statements for interacting with the 'audit' table in the 'my_schema' database.
 *
 * @author razlivinsky
 * @since 06.03.2024
 */
public class SchemaSql {
    /**
     * SQL statement to insert a new audit record into the 'audit' table.
     */
    public static final String INSERT_SCHEMA_AUDITION = "INSERT INTO my_schema.audit (message) VALUES (?)";
}