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

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CitilnkMainPage extends BasePage {

    private final String url = ConfigManager.get().citilinkUrl();
    private final String titleName = "Ситилинк – интернет-магазин техники, электроники, товаров для дома и ремонта";

    public CitilnkMainPage() {

    }

    @ElementName("Поле ввода поиска")
    private SelenideElement searchInput = $x("//input[@placeholder='Поиск по товарам']");

    @ElementName("Каталог товаров")
    private final SelenideElement catalogButton = getElementWithText("Каталог товаров");

    @ElementName("Войти")
    private final SelenideElement loginButton = getElementWithText("Войти");

    @ElementName("Корзина")
    private final SelenideElement basketButton = getElementWithText("Корзина");

    @ElementName("Счётчик кол-во добавленных товаров в корзину")
    private final SelenideElement notificationCounter =
            $x("//div[@data-meta-name='NotificationCounter']");

    @ElementName("Крестик всплывающего окна")
    private final SelenideElement closeWindowButton =
            $x("//div[@data-meta-name='UpsaleBasketLayout']//button[contains(@data-meta-name,'close-popup')]");

    private final String bestProductsComponentPath = "//h3[text()='Лучшие предложения']/ancestor::section";

    private final String productsPath = bestProductsComponentPath
            + "//div[@data-meta-name='ProductsCompilation__slide'][.//span[@data-meta-price]]";

    @ElementName("Товары")
    private final ElementsCollection products = $$x(productsPath);

    private final String productPricesPath = ".//span[@data-meta-price]";

    private final String productNamesPath = ".//a[@title]";

    @Step("Открываю главную страницу Ситилинк")
    public CitilnkMainPage open() {
        openPage(url, titleName);
        return this;
    }

    @Step("Отображены важные элементы страницы")
    public CitilnkMainPage verifyPageElements() {
        elementIsVisible(searchInput);
        elementIsVisible(loginButton);
        elementIsVisible(catalogButton);
        elementIsVisible(basketButton);
        isNotEmptyListOfElements(products);
        return this;
    }

    @Step("Добавлен в корзину товар из списка 'Лучшие предложения' под номером {index}")
    public CitilnkMainPage addItemOfBestProductInBasket(int index) {
        products.get(index)
                .shouldBe(visible)
                .scrollTo()
                .hover()
                .$x(".//button[@data-meta-name='Snippet__cart-button']")
                .shouldBe(interactable, Duration.ofSeconds(3))
                .click(usingJavaScript());
        if (closeWindowButton.is(visible, Duration.ofSeconds(15))) {
            try {
                closeWindowButton.click();
            } catch (Exception _) {
            }
        }
        return this;
    }

    @Step("Счётчик кол-во добавленных товаров в корзину равен {expectedValue}")
    public CitilnkMainPage checkValueInNotificationCounter(int expectedValue) {
        notificationCounter.shouldHave(attribute("data-meta-value", String.valueOf(expectedValue)));
        return this;
    }

    @Step("Добавлено {count} товаров в корзину")
    public CitilnkMainPage addProductsInBasket(int count) {
        for (int i = 0; i < count; i++) {
            addItemOfBestProductInBasket(i);
        }
        return this;
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

    @Step("Переход в корзину")
    public void goToBasket(){
        clickByElement(basketButton);
    }

    @Step("Выполнен поиск по запросу")
    public void searchByQuery(String query){
        inputTextInElement(searchInput, query);
        pressEnter(searchInput);
    }
}
