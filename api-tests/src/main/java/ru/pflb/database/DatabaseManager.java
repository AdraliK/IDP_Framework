package ru.pflb.database;

import ru.pflb.utils.DataKeys;
import ru.pflb.utils.DataStorage;

import java.sql.SQLException;

public class DatabaseManager {

    private final DatabaseClient db;

    public DatabaseManager(DatabaseClient db) {
        this.db = db;
    }

    private String getCurrentThreadId() {
        return DataStorage.get(DataKeys.THREAD_ID);
    }

    public void insertCar(int carId) {
        String sql = "INSERT INTO cars (id, threadId) VALUES (%d, '%s')".formatted(carId, getCurrentThreadId());
        executeUpdateSafely(sql, "Ошибка добавления id автомобиля в БД. CarId = " + carId);
    }

    public void insertUser(int userId) {
        String sql = "INSERT INTO users (id, threadId) VALUES (%d, '%s')".formatted(userId, getCurrentThreadId());
        executeUpdateSafely(sql, "Ошибка добавления id пользователя в БД. UserId = " + userId);
    }

    public int getCarId() {
        String sql = "SELECT id FROM cars WHERE threadId = '%s'".formatted(getCurrentThreadId());
        Object result = getSingleValueSafely(sql, "Ошибка получения ID автомобился из БД. ThreadId=" + getCurrentThreadId());
        return ((Number) result).intValue();
    }

    public int getUserId() {
        String sql = "SELECT id FROM users WHERE threadId = '%s'".formatted(getCurrentThreadId());
        Object result = getSingleValueSafely(sql, "Ошибка получения ID пользователя из БД. ThreadId=" + getCurrentThreadId());
        return ((Number) result).intValue();
    }

    public void deleteUser() {
        executeUpdateSafely("DELETE FROM users WHERE threadId = '%s'".formatted(getCurrentThreadId()),
                "Ошибка удаления пользователя из БД. ThreadId=" + getCurrentThreadId());
    }

    public void deleteCar() {
        executeUpdateSafely("DELETE FROM users WHERE threadId = '%s'".formatted(getCurrentThreadId()),
                "Ошибка удаления автомобился из БД. ThreadId=" + getCurrentThreadId());
    }

    private void executeUpdateSafely(String sql, String errorMessage) {
        try {
            db.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    private Object getSingleValueSafely(String sql, String errorMessage) {
        try {
            return db.getSingleValue(sql);
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }
}