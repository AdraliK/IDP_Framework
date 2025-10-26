package ru.pflb.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import ru.pflb.driver.DriverManager;

@Epic("UI тесты")
@Tag("ui")
public class BaseUiTests {

    @BeforeAll
    static void option() {
        DriverManager.initOptions();
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

}
