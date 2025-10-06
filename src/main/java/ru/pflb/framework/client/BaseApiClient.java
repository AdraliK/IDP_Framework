package ru.pflb.framework.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiClient.class);
    protected RequestSpecification requestSpec;

    public BaseApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    protected Response get(String endpoint) {
        return given()
                .spec(requestSpec)
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    protected Response post(String endpoint, Object body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    protected Response put(String endpoint, Object body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

}
