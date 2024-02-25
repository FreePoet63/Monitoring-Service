package com.ylab.app.constants;

/**
 * CreateSchemaSql class represents a utility class for SQL statements related to schema creation.
 *
 * This class provides static final Strings for inserting user data, meter data, reading data, and audit data into the specified schema.
 *
 * @author razlivinsky
 * @since 11.02.2024
 */
public class CreateSchemaSql {
    public static final String INSERT_USER_SCHEMA = "INSERT INTO my_schema.usr (username, password, role) VALUES (?, ?, ?)";
    public static final String INSERT_METER_SCHEMA = "INSERT INTO my_schema.mtr (number_Meter, date, user_name) VALUES (?, ?, ?)";
    public static final String INSERT_READING_DATA_SCHEMA = "INSERT INTO my_schema.mtr_readings (mtr_id, type, value) VALUES (?, ?, ?)";
    public static final String INSERT_SCHEMA_AUDITION = "INSERT INTO my_schema.audit (message) VALUES (?)";
}