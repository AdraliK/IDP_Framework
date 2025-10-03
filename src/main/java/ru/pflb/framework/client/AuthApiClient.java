package ru.pflb.framework.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.pflb.framework.dto.LoginRequestJson;

import static ru.pflb.framework.specification.RequestSpecs.requestBaseSpec;
import static ru.pflb.framework.specification.ResponseSpecs.successAuth;


public class AuthApiClient extends BaseApiClient {

    public AuthApiClient() {
        super(requestBaseSpec(), successAuth());
    }

    public AuthApiClient(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
        super(requestSpec, responseSpec);
    }

    public String login(String username, String password) {
        LoginRequestJson loginRequestJson = new LoginRequestJson(username, password);
        Response response = post("/login", loginRequestJson);
        try {
            return response.jsonPath().getString("access_token");
        } catch (Exception e) {
            return null;
        }
    }

}
