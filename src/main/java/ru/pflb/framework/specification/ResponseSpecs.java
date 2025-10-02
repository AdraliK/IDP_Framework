package ru.pflb.framework.specification;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecs {

    public static ResponseSpecification successAuth() {
        return new ResponseSpecBuilder()
                .expectStatusCode(202)
                .build();
    }

    public static ResponseSpecification unSuccessAuth() {
        return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .build();
    }

    public static ResponseSpecification success200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

}
