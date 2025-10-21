package ru.pflb.framework.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CustomConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);

    private static final Properties props = new Properties();
    private static final String[] files = {
            "application.properties",
            "secrets.properties"
    };

    static {
        try {
            for (String file : files) {
                try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(file)) {
                    if (is != null) {
                        props.load(is);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить свойства: " + e.getMessage(), e);
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Переменная %s не найдена!".formatted(key));
        }
        log.debug("Для переменной %s найдено значние".formatted(key));
        return value;
    }
}

