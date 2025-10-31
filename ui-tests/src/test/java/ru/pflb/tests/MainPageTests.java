package ru.pflb.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.pages.factory.Pages;


@Feature("Главная страница")
@Tag("mainPage")
@Severity(SeverityLevel.CRITICAL)
public class MainPageTests extends BaseUiTests {

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка корректного захода на страницу")
    void openMainPageTest() {
        Pages.main().open();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы")
    void mainPageElementsVisibilityTest() {
        Pages.main().open()
                .verifyPageElements();
    }

    @DisplayName("Провекрка работы счётчика корзины:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void addProductsInBasketTest(int count) {
        Pages.main().open()
                .addProductsInBasket(count)
                .checkValueInNotificationCounter(count);
    }

}
