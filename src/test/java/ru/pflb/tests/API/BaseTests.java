package ru.pflb.tests.API;

import io.qameta.allure.Allure;
import io.restassured.http.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pflb.framework.utils.DataKeys;
import ru.pflb.framework.utils.DataStorage;

import static ru.pflb.framework.steps.ApiSteps.*;

public class BaseTests {

    protected static final Logger log = LoggerFactory.getLogger(BaseTests.class);

    @BeforeEach
    void beforeEach(TestInfo info) {
        log.info("Запуск теста: {}", info.getDisplayName());
    }

    @AfterEach
    void afterEach(TestInfo info) {
        log.info("Завершение теста: {}", info.getDisplayName());
    }

}
