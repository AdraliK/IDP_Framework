package ru.pflb.steps.business;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import ru.pflb.dto.Car;
import ru.pflb.utils.DataKeys;
import ru.pflb.utils.DataStorage;

import static ru.pflb.steps.technical.ApiSteps.*;

public class CarBusinessApiSteps {

    @Step("Создаём новый автомобиль")
    public static Car createCar(Car userRequest) {
        Response response = sendRequest(Method.POST, "/car", userRequest);
        checkResponseStatusCode(response, 201);
        checkResponseMatchesDto(response, Car.class);
        Car car = response.as(Car.class);
        DataStorage.put(DataKeys.CAR_ID, String.valueOf(car.getId()));
        return car;
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
