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
    public static final String ALL_USERS = "SELECT * FROM my_schema.usr";
    public static final String FOUND_MAX_ID = "SELECT * FROM my_schema.mtr WHERE id = (SELECT MAX(id) FROM my_schema.mtr WHERE user_name = ?)";
    public static final String All_READINGS = "SELECT mv.type, mv.value FROM my_schema.mtr mr INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.id = ?";
    public static final String SELECT_USER_NAME = "SELECT * FROM my_schema.mtr WHERE user_name = ?";
    public static final String SELECT_ALL_METER_READINGS = "SELECT * FROM my_schema.mtr";
    public static final String FIND_USER_BY_ID = "SELECT * FROM my_schema.usr WHERE id = ?";
    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM my_schema.usr WHERE username = ?";
    public static final String GET_AUDIT = "SELECT * FROM my_schema.audit";
}