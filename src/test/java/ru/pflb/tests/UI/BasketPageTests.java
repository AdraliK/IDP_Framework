package ru.pflb.tests.UI;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.dto.ui.ProductData;
import ru.pflb.framework.pages.BasketPage;
import ru.pflb.framework.pages.CitilnkMainPage;
import ru.pflb.framework.utils.config.ConfigManager;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;

@Feature("Корзина пользователя")
@Tag("basketPage")
public class BasketPageTests extends BaseUiTests{

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы с пустой корзиной")
    void emptyBasketPageElementsVisibilityTest() {
        open(ConfigManager.get().citilinkUrl() + "/order/");
        BasketPage basketPage = new BasketPage();
        basketPage.elementIsVisible(basketPage.backToShopButton);
        basketPage.elementIsVisibleWithText("В корзине нет товаров");
    }

    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы с непустой корзиной:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void filedBasketPageElementsVisibilityTest(int count) {
        CitilnkMainPage mainPage = new CitilnkMainPage();

        mainPage.addProductsInBasket(count);

        mainPage.clickByElement(mainPage.basketButton);
        BasketPage basketPage = new BasketPage();
        basketPage.elementIsVisible(basketPage.regOrderButton);
        basketPage.elementIsVisible(basketPage.backToShopClickableText);
        basketPage.elementIsVisible(basketPage.totalSum);
        basketPage.isNotEmptyListOfElements(basketPage.products);
    }

    @DisplayName("Проверка суммирования добавленных товаров в корзине:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void shouldCalculateTotalSumCorrectly(int count) {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.addProductsInBasket(count);

        mainPage.checkValueInNotificationCounter(count);
        mainPage.clickByElement(mainPage.basketButton);

        BasketPage basketPage = new BasketPage();
        basketPage.checkTotalSumOrder();
    }

    @DisplayName("Проверка корректности содержимого корзины:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void shouldContainExactlySelectedProducts(int count) {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.isNotEmptyListOfElements(mainPage.products);

        List<ProductData> productDataListActual = mainPage.addProductsInBasketAndGetData(count);
        mainPage.clickByElement(mainPage.basketButton);

        BasketPage basketPage = new BasketPage();
        List<ProductData> productDataListExpected = basketPage.getProductsWithData();
        mainPage.checkEqualityLists(productDataListActual, productDataListExpected, "Данные товаров");
    }

}
