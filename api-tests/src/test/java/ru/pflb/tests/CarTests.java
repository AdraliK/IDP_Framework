package ru.pflb.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ru.pflb.dto.Car;
import ru.pflb.steps.business.CarBusinessApiSteps;
import ru.pflb.tests.hooks.BeforeTestApiHooks;
import ru.pflb.utils.JsonUtils;
import ru.pflb.utils.Operator;

import static ru.pflb.steps.CommonSteps.*;
import static ru.pflb.steps.technical.ApiSteps.*;


@Feature("Работа с автомобилями")
@Tag("car")
public class CarTests extends BeforeTestApiHooks {

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Провекрка жизненного цикла автомобиля")
    void carLifecycleTest() {
        Car carRequest = JsonUtils.getJsonAsPojo("cars/car.json", Car.class);

        Car createdCar = CarBusinessApiSteps.createCar(carRequest);
        int carId = createdCar.getId();
        carRequest.setId(carId);
        checkEqualsDto(carRequest, createdCar);

        Car fetchedCar = CarBusinessApiSteps.getCar(carId);
        checkEqualsDto(carRequest, fetchedCar);

        CarBusinessApiSteps.deleteCar(carId);
        Response response = sendRequest(
                Method.GET,
                "/car/" + carId,
                null
        );
        checkResponseStatusCode(response, 204);
        String responseBody = response.getBody().asString();
        checkComparison(responseBody, "{}", Operator.EQUALS);
    }

}
