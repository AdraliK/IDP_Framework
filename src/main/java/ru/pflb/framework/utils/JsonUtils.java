package ru.pflb.framework.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    protected static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final String BASE_PATH = "src/test/resources/data/";
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {

    }

    public static String getJsonString(String fileName) {
        Path path = Paths.get(BASE_PATH + fileName);
        try {
            String json = Files.readString(path);
            mapper.readTree(json);
            log.info("Получена строка из файла '%s':\n%s".formatted(path.toString(), json));
            return json;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл: " + path, e);
        } catch (Exception e) {
            throw new RuntimeException("Файл '%s' не соответсвует структуре JSON".formatted(fileName), e);
        }
    }

    public static <T> T getJsonAsPojo(String fileName, Class<T> clazz) {
        Path path = Paths.get(BASE_PATH + fileName);
        try {
            String json = Files.readString(path);
            T obj = mapper.readValue(json, clazz);
            log.info("Получен Pojo-бъект из файла '%s':\n%s".formatted(path.toString(), obj.toString()));
            return obj;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл: " + path, e);
        } catch (Exception e) {
            throw new RuntimeException("Файл '%s' не соответствует структуре '%s'".formatted(fileName, clazz.getSimpleName()), e);
        }
    }

    public static <T> String getPojoAsString(T object) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            log.info("Получена строка из Pojo-объекта '%s':\n%s".formatted(object.getClass().getSimpleName(), json));
            return json;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации объекта в JSON", e);
        }
    }

}
