package ru.pflb.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.pflb.annotations.ElementName;
import ru.pflb.dto.ProductData;
import ru.pflb.utils.Operator;

import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static ru.pflb.steps.CommonSteps.checkComparison;

public class BasketPage extends BasePage {

    public BasketPage() {
        checkTitleName("Оформление заказа");
    }

    @ElementName("Перейти к оформлению")
    public final SelenideElement regOrderButton = getElementWithText("Перейти к оформлению");

    @ElementName("Кнопка 'Вернуться к покупкам'")
    public final SelenideElement backToShopButton = $x("//a[text()='Вернуться к покупкам']");

    @ElementName("Анкор 'Вернуться к покупкам'")
    public final SelenideElement backToShopClickableText = $x("//span[text()='Вернуться к покупкам']");

    @ElementName("Итоговая сумма")
    public final SelenideElement totalSum =
            $x("//div[@data-meta-name='BasketSummary']//span[@data-meta-price]");

    private final String productsInBasketPath = "//div[@data-meta-name='BasketSnippet']";

    @ElementName("Товары")
    public final ElementsCollection products = $$x(productsInBasketPath);

    @ElementName("Цены товаров")
    public final ElementsCollection productPrices = $$x(productsInBasketPath
            + "//div[@data-meta-name='AvailableProductStatus__price']//span[@data-meta-price]");

    @ElementName("Имена товаров")
    public final ElementsCollection productNames = $$x(productsInBasketPath
            + "//a[@title]");

    @Step("Сумма заказа соответсвует сумме стоимости товаров")
    public void checkTotalSumOrder() {
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
