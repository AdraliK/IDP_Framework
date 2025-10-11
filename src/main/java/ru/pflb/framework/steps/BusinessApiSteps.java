package ru.pflb.framework.steps;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import ru.pflb.framework.dto.User;

import java.math.BigDecimal;

import static ru.pflb.framework.steps.ApiSteps.*;

public class BusinessApiSteps {

    @Step("Создаём нового пользователя")
    public static User createUser(User userRequest) {
        Response response = sendRequest(Method.POST, "/user", userRequest);
        checkResponseStatusCode(response, 201);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

    @Step("Получаем пользователя по ID: {userId}")
    public static User getUser(int userId) {
        Response response = sendRequest(Method.GET, "/user/" + userId, null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

    @Step("Удаляем пользователя по ID: {userId}")
    public static void deleteUser(int userId) {
        Response response = sendRequest(Method.DELETE, "/user/" + userId, null);
        checkResponseStatusCode(response, 204);
    }

    @Step("Начисляем пользователю {userId} сумму {amount}")
    public static User addMoneyToUser(int userId, BigDecimal amount) {
        Response response = sendRequest(Method.POST, "/user/%s/money/%s".formatted(userId, amount), null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

}
