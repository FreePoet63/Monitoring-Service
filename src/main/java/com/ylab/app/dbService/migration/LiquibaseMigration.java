package com.ylab.app.dbService.migration;

import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.ylab.app.util.DataReaderDatabase.*;

/**
 * The LiquibaseMigration class provides a method for performing Liquibase migration on the database.
 * It includes a method for executing Liquibase migration using the specified database connection details and schema information.
 * @author razlivinsky
 * @since 31.01.2024
 */
public class LiquibaseMigration {
    /**
     * Performs Liquibase migration on the database using the provided database connection details and schema information.
     */
    public void performLiquibaseMigration() {
        try (Connection conn = DriverManager.getConnection(
                getDatabaseData(URL) + getDatabaseData(DATABASE),
                getDatabaseData(USER),
                getDatabaseData(PASSWORD))) {
            try (Statement statement = conn.createStatement()) {String defaultSchemaName = getLiquibaseSchema(DEFAULT_SCHEMA_NAME);
                statement.execute("CREATE SCHEMA IF NOT EXISTS " + defaultSchemaName);

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
                new CommandScope(UpdateCommandStep.COMMAND_NAME)
                        .addArgumentValue("url", getDatabaseData(URL) + getDatabaseData(DATABASE))
                        .addArgumentValue("username", getDatabaseData(USER))
                        .addArgumentValue("password", getDatabaseData(PASSWORD))
                        .addArgumentValue("changeLogFile", "db/changelog/liquibase-changelog.xml")
                        .addArgumentValue("defaultSchemaName", defaultSchemaName)
                        .execute();

                System.out.println("Миграция успешно выполнена.");
            } catch (Exception e) {
                System.out.println("Ошибка при выполнении миграции:" + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("не удалось подключиться к базе данных: " + e.getMessage());
        }
    }
}