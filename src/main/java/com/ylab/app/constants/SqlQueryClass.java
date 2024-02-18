package com.ylab.app.constants;

/**
 * The SqlQueryClass class contains SQL queries used in the application.
 * It provides static String fields for various SQL queries such as insert, select, and update operations.
 * This class facilitates access to the SQL queries throughout the application code.
 *
 * @author razlivinsky
 * @since 03.02.2024
 */
public class SqlQueryClass {
    public static final String LOGIN = "SELECT * FROM my_schema.usr WHERE name = ? AND password = ?";
    public static final String ALL_USERS = "SELECT * FROM my_schema.usr";
    public static final String FOUND_MAX_ID = "SELECT MAX(id) FROM my_schema.mtr WHERE user_name = ?";
    public static final String All_READINGS = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.id = ?";
    public static final String SELECT_USER_NAME = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.user_name = ?";
    public static final String SELECT_ALL_METER_READINGS = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id";
    public static final String SELECT_SUM_VALUE = "SELECT mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.user_name = ? AND mv.type = ?";
    public static final String SELECT_METER = "SELECT * FROM my_schema.mtr WHERE id = ?";
    public static final String SELECT_READING_ID = "SELECT * FROM my_schema.mtr_readings WHERE mtr_id = ?";
    public static final String FIND_USER_BY_ID = "SELECT * FROM my_schema.usr WHERE id = ?";
    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM my_schema.usr WHERE name = ?";
}