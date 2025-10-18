package ru.pflb.framework.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.pflb.framework.annotations.ElementName;
import ru.pflb.framework.steps.api.technical.ApiSteps;
import ru.pflb.framework.utils.Config;
import ru.pflb.framework.utils.Operator;

import static com.codeborne.selenide.Selenide.$x;

public class CitilnkMainPage extends BasePage {


    public CitilnkMainPage() {
        openPage(Config.getProperty("citilink.url"));
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
    public final SelenideElement exitButtonWindow = $x("//button[contains(@class, 'Button-Close')]");

    public final SelenideElement bestProductsComponent =
            $x("//h3[text()='Лучшие предложения']/ancestor::section");

    public final ElementsCollection listOfBestProducts =
            bestProductsComponent.$$x(".//div[@data-meta-name='ProductsCompilation__slide']");

    @Step("Получено наименование товара из списка 'Лучшие предложения' под номером {index}")
    public String getNameOfBestProduct(int index) {
        SelenideElement nameElement = listOfBestProducts.get(index).$x(".//div[@data-meta-product-id]/a[@title]");
        return getAttributeValue(nameElement, "title");
    }

    @Step("Получена цена товара из списка 'Лучшие предложения' под номером {index}")
    public Integer getPriceOfBestProduct(int index) {
        SelenideElement priceElement = listOfBestProducts.get(index).$x(".//span[@data-meta-price]");
        String priceText = getAttributeValue(priceElement, "data-meta-price");
        return Integer.parseInt(priceText);
    }

    @Step("Добавлен в корзину товар из списка 'Лучшие предложения' под номером {index}")
    public void addItemOfBestProductInBasket(int index) {
        listOfBestProducts.get(index)
                .scrollTo()
                .hover()
                .$x(".//button[@data-meta-name='Snippet__cart-button']")
                .click();
        waitSeconds(5);
        clickByElement(exitButtonWindow);
    }

    @Step("Счётчик кол-во добавленных товаров в корзину равен {expectedValue}")
    public void checkValueInNotificationCounter(int expectedValue) {
        String valueText = notificationCounter.getAttribute("data-meta-value");
        int actualValue = Integer.parseInt(valueText != null ? valueText : "-1");
        ApiSteps.checkComparison(actualValue, expectedValue, Operator.EQUALS);
    }
}
