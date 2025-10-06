package ru.pflb.tests.API;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static ru.pflb.framework.steps.ApiSteps.login;
import static ru.pflb.framework.steps.ApiSteps.unSuccessLogin;

@Epic("API тесты")
@Feature("Авторизация")
@Severity(SeverityLevel.CRITICAL)
@Tag("api")
@Tag("auth")
@Tag("smoke")
public class AuthTests extends BaseTests {

    @DisplayName("Проверка успешной авторизации")
    @ParameterizedTest(name = "пользователь: {0}")
    @ValueSource(strings = {"user", "admin"})
    void successLoginTest(String user) {
        String authToken = login(user);
        assertNotNull(authToken, "Ожидали получить access_token, а получили null");
    }

    @Test
    @DisplayName("Провекрка неуспешной авторизации")
    void unSuccessLoginTest() {
        String authToken = unSuccessLogin("fakeUser", "fakePass");
        assertNull(authToken, "Ожидали получить пустое значение, а получили тело ответа");
    }

}
