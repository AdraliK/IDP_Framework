package ru.pflb.tests.api;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Epic("API тесты")
@Tag("api")
public class BaseApiTests {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiTests.class);

    @BeforeEach
    void beforeEach(TestInfo info) {
        log.info("Запуск теста: {}", info.getDisplayName());
    }

    @AfterEach
    void afterEach(TestInfo info) {
        log.info("Завершение теста: {}", info.getDisplayName());
    }

}
