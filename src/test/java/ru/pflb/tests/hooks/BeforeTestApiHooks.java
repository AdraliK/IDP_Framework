package ru.pflb.tests.hooks;

import org.junit.jupiter.api.BeforeEach;
import ru.pflb.framework.utils.DataKeys;
import ru.pflb.framework.utils.DataStorage;
import ru.pflb.tests.api.BaseApiTests;

import static ru.pflb.framework.steps.api.technical.ApiSteps.login;

public class BeforeTestApiHooks extends BaseApiTests {

    @BeforeEach
    void beforeLogin() {
        DataStorage.put(DataKeys.AUTH_TOKEN, login("user"));
    }

}
