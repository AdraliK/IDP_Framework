package ru.pflb.tests.API;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.dto.User;
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
        String authToken = DataStorage.get("authToken");

        User userRequest = JsonUtils.getJsonAsPojo("user.json", User.class);
        Response response = sendRequest(
                Method.POST,
                "/user",
                userRequest,
                authToken
        );
        checkResponseStatusCode(response, 201);
        checkResponseMatchesDto(response, User.class);
        User userResponse = response.as(User.class);
        userRequest.setId(userResponse.getId());
        checkEqualsDto(userRequest, userResponse);

        int userId = userResponse.getId();

        response = sendRequest(
                Method.GET,
                "/user/" + userId,
                null,
                authToken
        );
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        userResponse = response.as(User.class);
        checkEqualsDto(userRequest, userResponse);

        response = sendRequest(
                Method.DELETE,
                "/user/" + userId,
                null,
                authToken
        );
        checkResponseStatusCode(response, 204);

        response = sendRequest(
                Method.GET,
                "/user/" + userId,
                null,
                authToken
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
                userRequest,
                DataStorage.get("authToken")
        );
        checkResponseStatusCode(response, 400);
    }

    @Tag("cleanData")
    @DisplayName("Провекрка начисления денег прользователю")
    @ParameterizedTest(name = "начисление денег: {0}")
    @ValueSource(strings = {"50.00", "243.06"})
    void addMoneyToUserTest(String moneyAmount) {
        String authToken = DataStorage.get("authToken");

        User userRequest = JsonUtils.getJsonAsPojo("user.json", User.class);
        Response response = sendRequest(
                Method.POST,
                "/user",
                userRequest,
                authToken
        );
        checkResponseStatusCode(response, 201);
        User userResponse = response.as(User.class);
        int userId = userResponse.getId();
        DataStorage.put("userId", Integer.toString(userId));
        BigDecimal lastMoney = userResponse.getMoney();

        BigDecimal amount = new BigDecimal(moneyAmount);
        response = sendRequest(
                Method.POST,
                "/user/%s/money/%s".formatted(userId, amount),
                null,
                authToken
        );
        checkResponseStatusCode(response, 200);
        userResponse = response.as(User.class);
        BigDecimal nowMoney = userResponse.getMoney();
        BigDecimal addMoney = lastMoney.add(amount);
        checkComparison(nowMoney, addMoney, Operator.EQUALS);
    }

}
