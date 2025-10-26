package ru.pflb.pages;


import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import ru.pflb.annotations.ElementName;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(String query) {
        checkTitleName(query + " - купить по низкой цене в Ситилинк");
    }

    private final String productsPath = "//div[@data-meta-name='ProductVerticalSnippet']";

    @ElementName("Товары")
    public final ElementsCollection products = $$x(productsPath);

    @ElementName("Имена товаров")
    public final ElementsCollection productNames = $$x(productsPath
            + "//a[@data-meta-name='Snippet__title']");

    @Step("В списке товаров существует товар по запросу: {query}")
    public void checkProductNamesContains(List<String> productNames, String query) {
        log.info("Список: {}", productNames.toString());
        Allure.addAttachment("productNames", productNames.toString());
        assertThat(
                "В списке\n%s\nне содержится товар по запросу: %s".formatted(productNames.toString(), query),
                productNames,
                hasItem(containsString(query))
        );
    }
}
