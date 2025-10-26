package ru.pflb.utils;

import java.util.*;

public class DataStorage {

    private static final ThreadLocal<Map<String, String>> storage =
            ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, String value) {
        storage.get().put(key, value);
    }

    public static String get(String key) {
        return storage.get().get(key);
    }

    public static boolean contains(String key) {
        return storage.get().containsKey(key);
    }

    public static Map<String, String> getAll() {
        return Collections.unmodifiableMap(storage.get());
    }

    public static void remove(String key) {
        storage.get().remove(key);
    }

    public static void clear() {
        storage.get().clear();
    }

}
