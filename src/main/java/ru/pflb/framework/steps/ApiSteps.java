package ru.pflb.framework.steps;

import io.qameta.allure.Step;
import ru.pflb.framework.client.AuthApiClient;
import ru.pflb.framework.specification.RequestSpecs;
import ru.pflb.framework.specification.ResponseSpecs;
import ru.pflb.framework.utils.Config;

public class ApiSteps {

    @Step("Производится авторизация пользователя {user}")
    public static String login(String user) {
        String username = Config.getProperty(user + ".username");
        String password = Config.getProperty(user + ".password");

        AuthApiClient authClient = new AuthApiClient();
        return authClient.login(username, password);
    }

    @Step("Производится авторизация по логину {username}")
    public static String login(String username, String password) {
        AuthApiClient authClient = new AuthApiClient();
        return authClient.login(username, password);
    }

    @Step("Производится неуспешная авторизация по логину {username}")
    public static String unSuccessLogin(String username, String password) {
        AuthApiClient authClient = new AuthApiClient(RequestSpecs.requestBaseSpec(), ResponseSpecs.unSuccessAuth());
        return authClient.login(username, password);
    }

}
