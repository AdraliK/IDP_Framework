package ru.pflb.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.pages.CitilnkMainPage;


@Feature("Главная страница")
@Tag("mainPage")
@Severity(SeverityLevel.CRITICAL)
public class MainPageTests extends BaseUiTests {

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка корректного захода на страницу")
    void openMainPageTest() {
        new CitilnkMainPage();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы")
    void mainPageElementsVisibilityTest() {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.elementIsVisible(mainPage.searchInput);
        mainPage.elementIsVisible(mainPage.loginButton);
        mainPage.elementIsVisible(mainPage.catalogButton);
        mainPage.elementIsVisible(mainPage.basketButton);
    }

    @DisplayName("Провекрка работы счётчика корзины:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void addProductsInBasketTest(int count) {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.addProductsInBasket(count);
        mainPage.checkValueInNotificationCounter(count);
    }

}
