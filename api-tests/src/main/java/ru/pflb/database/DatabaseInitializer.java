package ru.pflb.database;

import java.sql.SQLException;

public class DatabaseInitializer {

    public static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public static DatabaseClient createDatabaseClient() {
        return new DatabaseClient(URL, USERNAME, PASSWORD);
    }

    public static void createTables(DatabaseClient dbClient) {
        DatabaseClient db = new DatabaseClient(URL, USERNAME, PASSWORD);

        try {
            db.executeUpdate("""
            CREATE TABLE IF NOT EXISTS cars (
                id INT PRIMARY KEY,
                threadId VARCHAR(64) NOT NULL
            );
        """);

            db.executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY,
                threadId VARCHAR(64) NOT NULL
            );
        """);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }

}
