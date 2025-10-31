package ru.pflb.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.dto.ProductData;
import ru.pflb.pages.factory.Pages;
import ru.pflb.steps.CommonSteps;

import java.util.List;

@Feature("Корзина пользователя")
@Tag("basketPage")
public class BasketPageTests extends BaseUiTests{

    @Test
    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы с пустой корзиной")
    void emptyBasketPageElementsVisibilityTest() {
        Pages.basket().open()
                .verifyEmptyBasketPageElements();
    }

    @Tag("smoke")
    @DisplayName("Провекрка отображения основных элементов страницы с непустой корзиной:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void filedBasketPageElementsVisibilityTest(int count) {
        Pages.main().open()
                .addProductsInBasket(count)
                .goToBasket();

        Pages.basket().verifyFiledBasketPageElements();
    }

    @DisplayName("Проверка суммирования добавленных товаров в корзине:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void shouldCalculateTotalSumCorrectly(int count) {
        Pages.main().open()
                .addProductsInBasket(count)
                .checkValueInNotificationCounter(count)
                .goToBasket();

        Pages.basket().checkTotalSumOrder();

    }

    @DisplayName("Проверка корректности содержимого корзины:")
    @ParameterizedTest(name = "добавление {0} товаров")
    @ValueSource(ints = {3})
    void shouldContainExactlySelectedProducts(int count) {
        List<ProductData> productDataListActual = Pages.main().open()
                .verifyPageElements()
                .addProductsInBasketAndGetData(count);

        Pages.main().goToBasket();

        List<ProductData> productDataListExpected = Pages.basket()
                .getProductsWithData();

        CommonSteps.checkEqualityLists(productDataListActual, productDataListExpected, "Данные товаров");
    }

}
