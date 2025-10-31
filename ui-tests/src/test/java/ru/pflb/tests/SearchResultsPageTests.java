package ru.pflb.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.pflb.pages.factory.Pages;

import java.util.List;

@Feature("Поиск товаров")
@Tag("searchResult")
public class SearchResultsPageTests extends BaseUiTests {

    @Tag("smoke")
    @DisplayName("Проверка поиска товара: ")
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"ноутбук asus", "телевизор SAMSUNG", "iPhone 15"})
    void findProductByQueryTest(String query) {
        Pages.main().open()
                .searchByQuery(query);

        Pages.searchResults()
                .checkTitleName(query + " - купить по низкой цене в Ситилинк");

        List<String> productNames = Pages.searchResults()
                .verifyPageElements()
                .getProductNames();

        Pages.searchResults()
                .checkProductNamesContains(productNames, query.toLowerCase());
    }

}
