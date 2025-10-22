package ru.pflb.tests.ui;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.framework.pages.CitilnkMainPage;
import ru.pflb.framework.pages.SearchResultsPage;

import java.util.List;

@Feature("Поиск товаров")
@Tag("searchResult")
public class SearchResultsPageTests extends BaseUiTests {

    @Tag("smoke")
    @DisplayName("Проверка поиска товара: ")
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"ноутбук asus", "телевизор SAMSUNG", "iPhone 15"})
    void findProductByQueryTest(String query) {
        CitilnkMainPage mainPage = new CitilnkMainPage();
        mainPage.inputTextInElement(mainPage.searchInput, query);
        mainPage.pressEnter(mainPage.searchInput);

        SearchResultsPage resultsPage = new SearchResultsPage(query);

        resultsPage.isNotEmptyListOfElements(resultsPage.products);
        List<String> productNames = resultsPage.getAttributeValuesFromCollection(
                resultsPage.productNames,
                "title"
        ).stream().map(String::toLowerCase).toList();

        resultsPage.checkProductNamesContains(productNames, query.toLowerCase());
    }

}
