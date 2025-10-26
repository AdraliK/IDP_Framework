package ru.pflb.specification;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import ru.pflb.utils.config.ConfigManager;

public class RequestSpecs {

    public static RequestSpecification requestBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get().baseUrl())
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();
    }

}
