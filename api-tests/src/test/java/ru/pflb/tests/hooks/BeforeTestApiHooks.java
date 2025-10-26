package ru.pflb.tests.hooks;

import org.junit.jupiter.api.BeforeEach;
import ru.pflb.tests.BaseApiTests;
import ru.pflb.utils.DataKeys;
import ru.pflb.utils.DataStorage;

import static ru.pflb.steps.technical.ApiSteps.login;

public class BeforeTestApiHooks extends BaseApiTests {

    @BeforeEach
    void beforeLogin() {
        DataStorage.put(DataKeys.AUTH_TOKEN, login("user"));
    }

}
