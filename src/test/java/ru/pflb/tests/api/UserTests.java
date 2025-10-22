package ru.pflb.tests.api;

import io.qameta.allure.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.dto.api.Car;
import ru.pflb.framework.dto.api.User;
import ru.pflb.framework.steps.api.business.CarBusinessApiSteps;
import ru.pflb.framework.steps.api.business.UserBusinessApiSteps;
import ru.pflb.framework.utils.DataKeys;
import ru.pflb.framework.utils.DataStorage;
import ru.pflb.framework.utils.JsonUtils;
import ru.pflb.framework.utils.Operator;
import ru.pflb.tests.hooks.BeforeTestApiHooks;

import java.math.BigDecimal;
import java.util.List;

import static ru.pflb.framework.steps.api.technical.ApiSteps.*;

@Feature("Работа с пользователями")
@Tag("user")
public class UserTests extends BeforeTestApiHooks {

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Провекрка жизненного цикла пользователя")
    void userLifecycleTest() {
        User userRequest = JsonUtils.getJsonAsPojo("users/user.json", User.class);
        
        User createdUser = UserBusinessApiSteps.createUser(userRequest);
        int userId = createdUser.getId();
        userRequest.setId(userId);
        checkEqualsDto(userRequest, createdUser);

        User fetchedUser = UserBusinessApiSteps.getUser(userId);
        checkEqualsDto(userRequest, fetchedUser);

        UserBusinessApiSteps.deleteUser(userId);
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
    void addUserWithInvalidDataTest() {
        String userRequest = JsonUtils.getJsonString("users/invalidUser.json");
        Response response = sendRequest(
                Method.POST,
                "/user",
                userRequest
        );
        checkResponseStatusCode(response, 400);
    }

    @Tag("cleanUserData")
    @DisplayName("Провекрка начисления денег прользователю")
    @ParameterizedTest(name = "начисление денег: {0}")
    @ValueSource(strings = {"50.00", "243.06"})
    void addMoneyToUserTest(String moneyAmount) {
        User user = JsonUtils.getJsonAsPojo("users/user.json", User.class);

        User createdUser = UserBusinessApiSteps.createUser(user);
        int userId = createdUser.getId();
        DataStorage.put(DataKeys.USER_ID, Integer.toString(userId));

        BigDecimal amount = new BigDecimal(moneyAmount);
        BigDecimal previousMoney = createdUser.getMoney();
        User updatedUser = UserBusinessApiSteps.addMoneyToUser(createdUser.getId(), amount);

        BigDecimal expectedMoney = previousMoney.add(amount);
        checkComparison(updatedUser.getMoney(), expectedMoney, Operator.EQUALS);
    }

    @Test
    @Tag("cleanUserData")
    @DisplayName("Проверка попытки начисления отрицательной суммы пользователю")
    void addNegativeMoneyToUserTest() {
        User user = JsonUtils.getJsonAsPojo("users/user.json", User.class);
        
        User createdUser = UserBusinessApiSteps.createUser(user);
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

    @Test
    @Tag("cleanUserData")
    @Tag("cleanCarData")
    @DisplayName("Проверка покупки автомобиля")
    void buyCarToUserTest() {
        User user = JsonUtils.getJsonAsPojo("users/userWithMoney.json", User.class);

        User createdUser = UserBusinessApiSteps.createUser(user);
        int userId = createdUser.getId();
        DataStorage.put(DataKeys.USER_ID, Integer.toString(userId));

        Car car = JsonUtils.getJsonAsPojo("cars/car.json", Car.class);
        Car createdCar = CarBusinessApiSteps.createCar(car);
        int carId = createdCar.getId();
        DataStorage.put(DataKeys.CAR_ID, Integer.toString(carId));

        User userAfterBuy = UserBusinessApiSteps.buyCarToUser(userId, carId);
        BigDecimal actualMoney = userAfterBuy.getMoney();
        BigDecimal expectedMoney = createdUser.getMoney().subtract(car.getPrice());
        checkComparison(actualMoney, expectedMoney, Operator.EQUALS);

        List<Car> cars = UserBusinessApiSteps.getUserCars(userId);
        checkListContains(cars, createdCar);
    }

    @Test
    @Tag("cleanUserData")
    @Tag("cleanCarData")
    @DisplayName("Покупка автомобиля при недостатке средств")
    void buyCarNoMoneyTest() {
        User user = JsonUtils.getJsonAsPojo("users/user.json", User.class);

        User createdUser = UserBusinessApiSteps.createUser(user);
        int userId = createdUser.getId();
        DataStorage.put(DataKeys.USER_ID, Integer.toString(userId));

        Car car = JsonUtils.getJsonAsPojo("cars/car.json", Car.class);
        Car createdCar = CarBusinessApiSteps.createCar(car);
        int carId = createdCar.getId();
        DataStorage.put(DataKeys.CAR_ID, Integer.toString(carId));

        Response response = sendRequest(Method.POST, "/user/%s/buyCar/%s".formatted(userId, carId), null);
        checkResponseStatusCode(response, 406);
    }

    @AfterEach
    void cleanAuthToken(TestInfo info) {
        if (info.getTags().contains("cleanUserData")) {
            cleanUserData();
        }
        if (info.getTags().contains("cleanCarData")) {
            cleanCarData();
        }

        DataStorage.remove(DataKeys.AUTH_TOKEN);
    }

    private void cleanUserData() {
        Allure.step("Очистка данных пользователя после теста", () -> {
            String authToken = DataStorage.get(DataKeys.AUTH_TOKEN);

            String userId = DataStorage.get(DataKeys.USER_ID);
            if (userId != null && authToken != null) {
                sendRequest(
                        Method.DELETE,
                        "/user/" + userId,
                        null
                );
            }
            DataStorage.remove(DataKeys.USER_ID);
        });
    }

    private void cleanCarData() {
        Allure.step("Очистка данных автомобиля после теста", () -> {
            String authToken = DataStorage.get(DataKeys.AUTH_TOKEN);
            String carId = DataStorage.get(DataKeys.CAR_ID);

            if (carId != null && authToken != null) {
                sendRequest(
                        Method.DELETE,
                        "/car/" + carId,
                        null
                );
            }

            DataStorage.remove(DataKeys.CAR_ID);
        });
    }

}
