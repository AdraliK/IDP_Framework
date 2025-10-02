package ru.pflb.framework.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    public BaseApiClient(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        this.requestSpec = requestSpec;
        this.responseSpec = responseSpec;
    }

    protected Response get(String endpoint) {
        return given()
                .spec(requestSpec)
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    protected Response post(String endpoint, Object body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    protected Response put(String endpoint, Object body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

}
