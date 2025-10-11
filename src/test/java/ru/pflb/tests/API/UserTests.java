package ru.pflb.tests.API;

import io.qameta.allure.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.dto.User;
import ru.pflb.framework.steps.BusinessApiSteps;
import ru.pflb.framework.utils.DataKeys;
import ru.pflb.framework.utils.DataStorage;
import ru.pflb.framework.utils.JsonUtils;
import ru.pflb.framework.utils.Operator;

import java.math.BigDecimal;

import static ru.pflb.framework.steps.ApiSteps.*;

@Epic("API тесты")
@Feature("Работа с пользователями")
@Tag("api")
@Tag("user")
public class UserTests extends AuthTests {

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Провекрка жизненного цикла пользователя")
    void userLifecycleTest() {
        User userRequest = JsonUtils.getJsonAsPojo("user.json", User.class);

        User createdUser = BusinessApiSteps.createUser(userRequest);
        int userId = createdUser.getId();
        userRequest.setId(userId);
        checkEqualsDto(userRequest, createdUser);

        User fetchedUser = BusinessApiSteps.getUser(userId);
        checkEqualsDto(userRequest, fetchedUser);

        BusinessApiSteps.deleteUser(userId);
        Response response = sendRequest(
                Method.GET,
                "/user/" + userId,
                null
        );
        checkResponseStatusCode(response, 204);
        String responseBody = response.getBody().asString();
        checkComparison(responseBody, "{}", Operator.EQUALS);
    }

    @Test
    @DisplayName("Провекрка попытки добавления невалидного пользователя")
    void addUserWithInvalidData() {
        String userRequest = JsonUtils.getJsonString("invalidUser.json");
        Response response = sendRequest(
                Method.POST,
                "/user",
                userRequest
        );
        checkResponseStatusCode(response, 400);
    }

    @Tag("cleanData")
    @DisplayName("Провекрка начисления денег прользователю")
    @ParameterizedTest(name = "начисление денег: {0}")
    @ValueSource(strings = {"50.00", "243.06"})
    void addMoneyToUserTest(String moneyAmount) {
        User user = JsonUtils.getJsonAsPojo("user.json", User.class);

        User createdUser = BusinessApiSteps.createUser(user);
        int userId = createdUser.getId();
        DataStorage.put(DataKeys.USER_ID, Integer.toString(userId));

        BigDecimal amount = new BigDecimal(moneyAmount);
        BigDecimal previousMoney = createdUser.getMoney();
        User updatedUser = BusinessApiSteps.addMoneyToUser(createdUser.getId(), amount);

        BigDecimal expectedMoney = previousMoney.add(amount);
        checkComparison(updatedUser.getMoney(), expectedMoney, Operator.EQUALS);
    }

    @Test
    @Tag("cleanData")
    @DisplayName("Проверка попытки начисления отрицательной суммы пользователю")
    void addNegativeMoneyToUserTest() {
        User user = JsonUtils.getJsonAsPojo("user.json", User.class);
        User createdUser = BusinessApiSteps.createUser(user);
        int userId = createdUser.getId();
        DataStorage.put(DataKeys.USER_ID, Integer.toString(userId));

        BigDecimal negativeAmount = new BigDecimal("-50.00");

        Response response = sendRequest(
                Method.POST,
                "/user/%s/money/%s".formatted(userId, negativeAmount),
                null
        );

        checkResponseStatusCode(response, 400);
    }

    @AfterEach
    void cleanData(TestInfo info) {
        if (!info.getTags().contains("cleanData")) return;

        Allure.step("Очистка данных после теста", () -> {
            String authToken = DataStorage.get(DataKeys.AUTH_TOKEN);
            String userId = DataStorage.get(DataKeys.USER_ID);

            if (userId != null && authToken != null) {
                sendRequest(
                        Method.DELETE,
                        "/user/" + userId,
                        null
                );
            }

            DataStorage.clear();
        });
    }

}
