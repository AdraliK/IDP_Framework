package ru.pflb.tests.API;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.client.AuthApiClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.pflb.framework.specification.RequestSpecs.requestBaseSpec;
import static ru.pflb.framework.specification.ResponseSpecs.unSuccessAuth;
import static ru.pflb.framework.steps.ApiSteps.login;

public class BaseTests {

    @Feature("Тестирование авторизации")
    @ParameterizedTest(name = "Проверка успешной авторизации пользователя: {0}")
    @ValueSource(strings = {"user", "admin"})
    void successLoginTest(String username) {
        String authToken = login(username);
        assertNotNull(authToken, "Ожидали получить access_token, а получили пустое значение");
    }

    @Feature("Тестирование авторизации")
    @Test
    @DisplayName("Провекрка неуспешной авторизации")
    public void unSuccessLoginTest() {
        AuthApiClient apiClient = new AuthApiClient(requestBaseSpec(), unSuccessAuth());
        String authToken = apiClient.login("fakeUser", "fakePass");
        assertNull(authToken, "Ожидали получить пустое значение, а получили access_token");
    }

}
