package ru.pflb.framework.steps.api.technical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pflb.framework.client.AuthApiClient;
import ru.pflb.framework.client.AuthorizedApiClient;
import ru.pflb.framework.utils.*;
import ru.pflb.framework.utils.config.ConfigManager;
import ru.pflb.framework.utils.config.CustomConfigManager;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiSteps {

    private static final Logger log = LoggerFactory.getLogger(ApiSteps.class);

    @Step("Авторизуемся под пользователем {user}")
    public static String login(String user) {
        String username = CustomConfigManager.getProperty(user + ".username");
        String password = CustomConfigManager.getProperty(user + ".password");

        AuthApiClient authClient = new AuthApiClient();
        return authClient.login(username, password);
    }

    @Step("Авторизуемся по логину {username}")
    public static String login(String username, String password) {
        AuthApiClient authClient = new AuthApiClient();
        return authClient.login(username, password);
    }

    @Step("Производим неуспешную авторизацию по логину {username}")
    public static String unSuccessLogin(String username, String password) {
        AuthApiClient authClient = new AuthApiClient();
        return authClient.login(username, password);
    }

    @Step("Выполнен {method} запрос на endpoint '{endpoint}' с токеном авторизаци")
    public static Response sendRequest(Method method, String endpoint, Object body) {
        AuthorizedApiClient apiClient = new AuthorizedApiClient(DataStorage.get(DataKeys.AUTH_TOKEN));
        return apiClient.sendRequest(method, endpoint, body);
    }

    @Step("Статус-код ответа равен '{statusCode}'")
    public static void checkResponseStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Из тела ответа получено значение по JsonPath '{jsonPath}'")
    public static Object getJsonPathValue(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        log.info("По JsonPath = '{}' получено значение: {}", jsonPath, value);
        return value;
    }

    @Step("Из тела ответа получен список по JsonPath '{jsonPath}'")
    public static <T> List<T> getJsonPathListFromResponse(Response response, String jsonPath, Class<T> type) {
        String responseBody = response.getBody().asString();

        if (responseBody == null || responseBody.isBlank() || responseBody.equals("[]")) {
            log.warn("Ответ пустой или содержит пустой массив");
            return Collections.emptyList();
        }

        List<T> list;
        try {
            list = response.jsonPath().getList(jsonPath, type);
        } catch (Exception e) {
            throw new AssertionError("Невозможно получить список из ответа", e);
        }

        if (list == null || list.isEmpty()) {
            log.warn("По JsonPath '{}' список пустой или отсутствует", jsonPath);
        } else {
            log.info("По JsonPath '{}' получен список из {} элементов: {}", jsonPath, list.size(), list);
            Allure.addAttachment("Список элементов: ", JsonUtils.getPojoAsString(list));
        }

        return list;
    }

    public static <T> List<T> getListFromResponse(Response response, Class<T> type) {
        String objectName = type.getSimpleName();
        Allure.step("Из тела ответа получен список элементов '%s'".formatted(objectName), () -> {});

        String responseBody = response.getBody().asString();

        ObjectMapper mapper = new ObjectMapper();
        List<T> list;
        try {
            list = mapper.readValue(
                    responseBody,
                    mapper.getTypeFactory().constructCollectionType(List.class, type)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Step("Список содержит элемент '{expectedValue}'")
    public static <T> void checkListContains(List<T> list, T expectedValue) {
        Allure.addAttachment("Список", list.toString());
        assertThat(
                String.format("Ожидалось, что список будет содержать значение '%s'", expectedValue),
                list,
                hasItem(expectedValue)
        );
    }

    @Step("Список не содержит элемент '{expectedValue}'")
    public static <T> void checkListNotContains(List<T> list, T expectedValue) {
        Allure.addAttachment("Список", list.toString());
        assertThat(
                String.format("Ожидалось, что список будет содержать значение '%s'", expectedValue),
                list,
                not(hasItem(expectedValue))
        );
    }

    @Step("Значение '{actualValue}' {operator} '{expectedValue}'")
    public static <T extends Comparable<T>> void checkComparison(T actualValue, T expectedValue, Operator operator) {
        switch (operator) {
            case EQUALS -> assertThat(
                    "Ожидалось, что значение будет равно " + expectedValue + ", но получили " + actualValue,
                    actualValue,
                    equalTo(expectedValue)
            );
            case NOT_EQUALS -> assertThat(
                    "Ожидалось, что значение не будет равно " + expectedValue + ", но получили " + actualValue,
                    actualValue,
                    not(equalTo(expectedValue))
            );
            case GREATER -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет больше чем " + expectedValue,
                    actualValue.compareTo(expectedValue) > 0
            );
            case LESS -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет меньше чем " + expectedValue,
                    actualValue.compareTo(expectedValue) < 0
            );
            case GREATER_OR_EQUAL -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет больше или равно " + expectedValue,
                    actualValue.compareTo(expectedValue) >= 0
            );
            case LESS_OR_EQUAL -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет меньше или равно " + expectedValue,
                    actualValue.compareTo(expectedValue) <= 0
            );
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }

    public static <T> void checkResponseMatchesDto(Response response, Class<T> dtoClass) {
        String dtoName = dtoClass.getSimpleName();
        Allure.step("Тело ответа соответствует DTO '%s'".formatted(dtoName), () -> {
            T obj = response.as(dtoClass);
            assertNotNull(obj, "Не удалось десериализовать ответ в %s. Тело ответа: \n %s"
                    .formatted(dtoName, response.getBody()));
        });
    }

    public static <T> void checkEqualsDto(T actualObject, T expectedObject) {
        String actualName = actualObject.getClass().getSimpleName();
        String expectedName = expectedObject.getClass().getSimpleName();

        Allure.step("Объект '%s' равен объекту '%s'".formatted(actualName, expectedName), () -> {
            String actualJson = JsonUtils.getPojoAsString(actualObject);
            String expectedJson = JsonUtils.getPojoAsString(expectedObject);

            log.info("actualObject {}:\n{}", actualName, actualJson);
            log.info("expectedObject {}:\n{}", expectedName, expectedJson);

            Allure.addAttachment(actualName, actualJson);
            Allure.addAttachment(expectedName, expectedJson);

            assertThat("Объекты не равны", actualObject, equalTo(expectedObject));
        });
    }


}
