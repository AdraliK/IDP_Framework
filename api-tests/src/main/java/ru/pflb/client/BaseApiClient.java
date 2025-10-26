package ru.pflb.client;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static ru.pflb.specification.RequestSpecs.requestBaseSpec;

public abstract class BaseApiClient {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiClient.class);
    protected RequestSpecification requestSpec;

    protected static RequestSpecification authorizedSpec(String authToken) {
        return requestBaseSpec().header("Authorization", "Bearer " + authToken);
    }

    public BaseApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public Response sendRequest(Method method, String endpoint, Object body) {
        RequestSpecification spec = given()
                .spec(requestSpec);

        if (body != null) {
            spec.body(body);
        }

        return spec
                .request(method, endpoint)
                .then()
                .extract()
                .response();
    }

}
