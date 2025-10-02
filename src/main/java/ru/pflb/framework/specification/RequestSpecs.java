package ru.pflb.framework.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import ru.pflb.framework.utils.Config;

public class RequestSpecs {

    public static RequestSpecification requestBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.getProperty("base.url"))
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

}
