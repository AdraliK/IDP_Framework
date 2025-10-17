package ru.pflb.tests.UI;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.pages.CitilnkMainPage;


@Epic("UI тесты")
@Feature("Главная страница")
@Severity(SeverityLevel.CRITICAL)
@Tag("ui")
public class MainPageTests extends BaseUiTest {

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка корректного захода на страницу")
    void openMainPageTest() {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.checkTitleName("Ситилинк – интернет-магазин техники, электроники, товаров для дома и ремонта");
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

    @DisplayName("Провекрка работы счётчика корзины")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void addProductsInBasketTest(int count) {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        for (int i = 0; i < count; i++) {
            mainPage.addItemOfBestProductInBasket(i);
        }
        mainPage.checkValueInNotificationCounter(count);
    }

}
