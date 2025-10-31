package ru.pflb.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.pflb.annotations.ElementName;
import ru.pflb.dto.ProductData;
import ru.pflb.utils.Operator;
import ru.pflb.utils.config.ConfigManager;

import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static ru.pflb.steps.CommonSteps.checkComparison;

public class BasketPage extends BasePage {

    private final String url = ConfigManager.get().citilinkUrl() + "/order/";
    private final String titleName = "Оформление заказа";

    public BasketPage() {

    }

    @ElementName("Перейти к оформлению")
    private final SelenideElement regOrderButton = getElementWithText("Перейти к оформлению");

    @ElementName("Кнопка 'Вернуться к покупкам'")
    private final SelenideElement backToShopButton = $x("//a[text()='Вернуться к покупкам']");

    @ElementName("Анкор 'Вернуться к покупкам'")
    private final SelenideElement backToShopClickableText = $x("//span[text()='Вернуться к покупкам']");

    @ElementName("Итоговая сумма")
    private final SelenideElement totalSum =
            $x("//div[@data-meta-name='BasketSummary']//span[@data-meta-price]");

    private final String productsInBasketPath = "//div[@data-meta-name='BasketSnippet']";

    @ElementName("Товары")
    private final ElementsCollection products = $$x(productsInBasketPath);

    @ElementName("Цены товаров")
    private final ElementsCollection productPrices = $$x(productsInBasketPath
            + "//div[@data-meta-name='AvailableProductStatus__price']//span[@data-meta-price]");

    @ElementName("Имена товаров")
    private final ElementsCollection productNames = $$x(productsInBasketPath
            + "//a[@title]");

    @Step("Открываю страницу корзины")
    public BasketPage open() {
        openPage(url, titleName);
        return this;
    }


    @Step("Отображены важные элементы пустой корзины")
    public BasketPage verifyEmptyBasketPageElements() {
        elementIsVisible(backToShopButton);
        elementIsVisibleWithText("В корзине нет товаров");
        return this;
    }

    @Step("Отображены важные элементы не пустой корзины")
    public BasketPage verifyFiledBasketPageElements() {
        elementIsVisible(regOrderButton);
        elementIsVisible(backToShopClickableText);
        elementIsVisible(totalSum);
        isNotEmptyListOfElements(products);
        return this;
    }

    @Step("Сумма заказа соответсвует сумме стоимости товаров")
    public BasketPage checkTotalSumOrder() {
        List<String> listOfProductsPrice = getAttributeValuesFromCollection(
                productPrices,
                "data-meta-price"
        );
        int expectedSum = listOfProductsPrice
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        String actualSumText = totalSum.shouldBe(visible).getAttribute("data-meta-price");
        actualSumText = actualSumText != null ? actualSumText : "-1";
        int actualSum = Integer.parseInt(actualSumText);

        checkComparison(actualSum, expectedSum, Operator.EQUALS);
        return this;
    }

    @Step("Получен список товаров из корзины с данными")
    public List<ProductData> getProductsWithData() {
        List<String> namesInBasket = getAttributeValuesFromCollection(productNames, "title");
        List<String> pricesInBasket = getAttributeValuesFromCollection(productPrices, "data-meta-price");

        return IntStream.range(0, namesInBasket.size())
                .mapToObj(i -> new ProductData(namesInBasket.get(i), pricesInBasket.get(i)))
                .toList();
    }

}
