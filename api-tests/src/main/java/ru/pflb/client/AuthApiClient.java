package ru.pflb.client;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.pflb.dto.LoginRequestJson;

import static ru.pflb.specification.RequestSpecs.requestBaseSpec;


public class AuthApiClient extends BaseApiClient {

    public AuthApiClient() {
        super(requestBaseSpec());
    }

    public AuthApiClient(RequestSpecification requestSpec) {
        super(requestSpec);
    }

    public String login(String username, String password) {
        LoginRequestJson loginRequestJson = new LoginRequestJson(username, password);
        Response response = sendRequest(Method.POST, "/login", loginRequestJson);
        if (response.statusCode() == 202) {
            synchronized(JsonPath.class) {
                return response.jsonPath().getString("access_token");
            }
        } else {
            log.error("Ошибка при авторизации!");
            return null;
        }
    }

}
