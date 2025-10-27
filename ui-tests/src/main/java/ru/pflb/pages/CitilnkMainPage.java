package ru.pflb.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.pflb.annotations.ElementName;
import ru.pflb.dto.ProductData;
import ru.pflb.utils.config.ConfigManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class CitilnkMainPage extends BasePage {

    public CitilnkMainPage() {
        openPage(ConfigManager.get().citilinkUrl());
        checkTitleName("Ситилинк – интернет-магазин техники, электроники, товаров для дома и ремонта");
    }

    @ElementName("Поле ввода поиска")
    public SelenideElement searchInput = $x("//input[@placeholder='Поиск по товарам']");

    @ElementName("Каталог товаров")
    public final SelenideElement catalogButton = getElementWithText("Каталог товаров");

    @ElementName("Войти")
    public final SelenideElement loginButton = getElementWithText("Войти");

    @ElementName("Корзина")
    public final SelenideElement basketButton = getElementWithText("Корзина");

    @ElementName("Счётчик кол-во добавленных товаров в корзину")
    public final SelenideElement notificationCounter =
            $x("//div[@data-meta-name='NotificationCounter']");

    @ElementName("Крестик всплывающего окна")
    public final SelenideElement closeWindowButton =
            $x("//div[@data-meta-name='UpsaleBasketLayout']//button[contains(@data-meta-name,'close-popup')]");

    private final String bestProductsComponentPath = "//h3[text()='Лучшие предложения']/ancestor::section";

    private final String productsPath = bestProductsComponentPath
            + "//div[@data-meta-name='ProductsCompilation__slide'][.//span[@data-meta-price]]";

    @ElementName("Товары")
    public final ElementsCollection products = $$x(productsPath);

    public final String productPricesPath = ".//span[@data-meta-price]";

    public final String productNamesPath = ".//a[@title]";

    @Step("Добавлен в корзину товар из списка 'Лучшие предложения' под номером {index}")
    public void addItemOfBestProductInBasket(int index) {
        products.get(index)
                .scrollTo()
                .hover()
                .$x(".//button[@data-meta-name='Snippet__cart-button']")
                .click();
        if (closeWindowButton.is(visible, Duration.ofSeconds(15))) {
            closeWindowButton.click();
        }
    }

    @Step("Счётчик кол-во добавленных товаров в корзину равен {expectedValue}")
    public void checkValueInNotificationCounter(int expectedValue) {
        notificationCounter.shouldHave(attribute("data-meta-value", String.valueOf(expectedValue)));
    }

    @Step("Добавлено {count} товаров в корзину")
    public void addProductsInBasket(int count) {
        for (int i = 0; i < count; i++) {
            addItemOfBestProductInBasket(i);
        }
    }

    @Step("Добавлено {count} товаров в корзину с сохранением данных")
    public List<ProductData> addProductsInBasketAndGetData(int count) {
        List<ProductData> productDataList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            addItemOfBestProductInBasket(i);

            SelenideElement elementPrice = products.get(i).$x(productPricesPath);
            String price = getAttributeValueStep(elementPrice, "data-meta-price", "Цена товара");

            SelenideElement elementName = products.get(i).$x(productNamesPath);
            String name = getAttributeValueStep(elementName, "title", "Наименование товара");

            productDataList.add(new ProductData(name, price));
        }

        return productDataList;
    }
}
