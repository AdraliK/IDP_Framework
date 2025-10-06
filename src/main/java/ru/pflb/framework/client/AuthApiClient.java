package ru.pflb.framework.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.pflb.framework.dto.LoginRequestJson;

import static ru.pflb.framework.specification.RequestSpecs.requestBaseSpec;


public class AuthApiClient extends BaseApiClient {

    public AuthApiClient() {
        super(requestBaseSpec());
    }

    public AuthApiClient(RequestSpecification requestSpec) {
        super(requestSpec);
    }

    public String login(String username, String password) {
        LoginRequestJson loginRequestJson = new LoginRequestJson(username, password);
        Response response = post("/login", loginRequestJson);
        if (response.statusCode() == 202) {
            return response.jsonPath().getString("access_token");
        } else {
            log.error("Ошибка при авторизации!");
            return null;
        }
    }

}
