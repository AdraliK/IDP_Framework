package ru.pflb.framework.steps.api.business;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import ru.pflb.framework.dto.api.Car;

import static ru.pflb.framework.steps.api.technical.ApiSteps.*;
import static ru.pflb.framework.steps.api.technical.ApiSteps.checkResponseStatusCode;

public class CarBusinessApiSteps {

    @Step("Создаём новый автомобиль")
    public static Car createCar(Car userRequest) {
        Response response = sendRequest(Method.POST, "/car", userRequest);
        checkResponseStatusCode(response, 201);
        checkResponseMatchesDto(response, Car.class);
        return response.as(Car.class);
    }

    @Step("Получаем автомобиль с ID: {carId}")
    public static Car getCar(int carId) {
        Response response = sendRequest(Method.GET, "/car/" + carId, null);
        checkResponseStatusCode(response, 200);
        checkResponseMatchesDto(response, Car.class);
        return response.as(Car.class);
    }

    @Step("Удаляем автомобиль с ID: {carId}")
    public static void deleteCar(int carId) {
        Response response = sendRequest(Method.DELETE, "/car/" + carId, null);
        checkResponseStatusCode(response, 204);
    }

}
