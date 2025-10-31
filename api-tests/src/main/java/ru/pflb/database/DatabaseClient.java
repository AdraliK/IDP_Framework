package ru.pflb.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseClient {

    private static final Logger log = LoggerFactory.getLogger(DatabaseClient.class);

    private final String url;
    private final String username;
    private final String password;

    public DatabaseClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        log.info("Подключенике к БД");
        return DriverManager.getConnection(url, username, password);
    }

    public void executeUpdate(String sql) throws SQLException {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            log.info("Выполен запрос к БД. SQL: {}", sql);
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения запроса: " + sql, e);
        }
    }

    public Object getSingleValue(String sql) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getObject(1);
            }
            throw new RuntimeException("Отсутствуют данные в БД по запросу SQL: " + sql);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения значения из БД по запросу SQL: " + sql, e);
        }
    }
}