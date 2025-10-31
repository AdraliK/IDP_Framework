package ru.pflb.steps.business;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import ru.pflb.dto.Car;
import ru.pflb.dto.User;
import ru.pflb.utils.DataKeys;
import ru.pflb.utils.DataStorage;

import java.math.BigDecimal;
import java.util.List;

import static ru.pflb.steps.technical.ApiSteps.*;

public class UserBusinessApiSteps {

    @Step("Создаём нового пользователя")
    public static User createUser(User userRequest) {
        Response response = sendRequest(Method.POST, "/user", userRequest);
        checkResponseStatusCode(response, 201);
        checkResponseMatchesDto(response, User.class);
        User user = response.as(User.class);
        DataStorage.put(DataKeys.USER_ID, String.valueOf(user.getId()));
        return user;
    }

    @Step("Получаем пользователя с ID: {userId}")
    public static User getUser(int userId) {
        Response response = sendRequest(Method.GET, "/user/" + userId, null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

    @Step("Удаляем пользователя с ID: {userId}")
    public static void deleteUser(int userId) {
        Response response = sendRequest(Method.DELETE, "/user/" + userId, null);
        checkResponseStatusCode(response, 204);
    }

    @Step("Начисляем пользователю c ID '{userId}' сумму '{amount}'")
    public static User addMoneyToUser(int userId, BigDecimal amount) {
        Response response = sendRequest(Method.POST, "/user/%s/money/%s".formatted(userId, amount), null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

    @Step("Пользователь c ID '{userId}' покупает автомобиль c ID '{carId}'")
    public static User buyCarToUser(int userId, int carId) {
        Response response = sendRequest(Method.POST, "/user/%s/buyCar/%s".formatted(userId, carId), null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, User.class);
        return response.as(User.class);
    }

    @Step("Получен список автомобилей пользователя с ID '{userId}'")
    public static List<Car> getUserCars(int userId) {
        Response response = sendRequest(Method.GET, "/user/%s/cars".formatted(userId), null);
        checkResponseStatusCode(response, 200);
        return getListFromResponse(response, Car.class);
    }

}
