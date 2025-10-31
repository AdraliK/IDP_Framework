package ru.pflb.pages.factory;

import ru.pflb.pages.BasketPage;
import ru.pflb.pages.CitilnkMainPage;
import ru.pflb.pages.SearchResultsPage;

public class Pages {

    private Pages() {

    }

    public static CitilnkMainPage main() {
        return new CitilnkMainPage();
    }

    public static BasketPage basket() {
        return new BasketPage();
    }

    public static SearchResultsPage searchResults() {
        return new SearchResultsPage();
    }

}
