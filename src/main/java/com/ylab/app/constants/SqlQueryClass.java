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
    public static String insertQuery = "INSERT INTO my_schema.usr (name, password, role) VALUES (?, ?, ?)";
    public static String loginQuery = "SELECT * FROM my_schema.usr WHERE name = ? AND password = ?";
    public static String queryAllUsers = "SELECT * FROM my_schema.usr";
    public static String insertMtrSQL = "INSERT INTO my_schema.mtr (number_Meter, date, user_name) VALUES (?, ?, ?)";
    public static String insertReadingSQL = "INSERT INTO my_schema.mtr_readings (mtr_id, type, value) VALUES (?, ?, ?)";
    public static String selectMaxIdSQL = "SELECT MAX(id) FROM my_schema.mtr WHERE user_name = ?";
    public static String selectAllSQL = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.id = ?";
    public static String selectUserNameSQL = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.user_name = ?";
    public static String selectAllMeterReadingSQL = "SELECT mr.*, mv.type, mv.value FROM my_schema.mtr mr INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id";
    public static String selectSumValueSQL = "SELECT mv.value FROM my_schema.mtr mr " +
            "INNER JOIN my_schema.mtr_readings mv ON mr.id = mv.mtr_id WHERE mr.user_name = ? AND mv.type = ?";
    public static String selectMtrSQL = "SELECT * FROM my_schema.mtr WHERE id = ?";
    public static String selectReadingSQL = "SELECT * FROM my_schema.mtr_readings WHERE mtr_id = ?";
}