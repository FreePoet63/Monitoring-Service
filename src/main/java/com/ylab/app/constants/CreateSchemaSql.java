package com.ylab.app.constants;

/**
 * CreateSchemaSql class
 *
 * @author HP
 * @since 11.02.2024
 */
public class CreateSchemaSql {
    public static final String INSERT_USER_SCHEMA = "INSERT INTO my_schema.usr (name, password, role) VALUES (?, ?, ?)";
    public static final String INSERT_METER_SCHEMA = "INSERT INTO my_schema.mtr (number_Meter, date, user_name) VALUES (?, ?, ?)";
    public static final String INSERT_READING_DATA_SCHEMA = "INSERT INTO my_schema.mtr_readings (mtr_id, type, value) VALUES (?, ?, ?)";
}