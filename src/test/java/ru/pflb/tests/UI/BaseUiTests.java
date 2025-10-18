package ru.pflb.tests.UI;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ru.pflb.framework.core.DriverManager;

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
