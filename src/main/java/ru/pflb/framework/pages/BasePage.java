package ru.pflb.framework.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pflb.framework.utils.ElementUtils;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class BasePage {

    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    @Step("Открываю страницу: {url}")
    public void openPage(String url) {
        open(url);
    }

    @Step("Открыта страница с именем: {titleName}")
    public void checkTitleName(String titleName) {
        webdriver().shouldHave(WebDriverConditions.title(titleName));
    }

    @Step("Ожидаю {seconds} секунд")
    public void waitSeconds(int seconds) {
        sleep(seconds * 1000L);
    }

    @Step("Клик по тексту: {text}")
    public void clickByElementWithText(String text) {
        $x(String.format("//*[text()='%s']", text)).click();
    }

    public void clickByElement(SelenideElement element) {
        String elementName = ElementUtils.getElementName(this, element);
        Allure.step("Клик на элемент ".concat(elementName), () -> {
            element.shouldBe(visible).click();
        });
    }

    public void elementIsVisible(SelenideElement element) {
        String elementName = ElementUtils.getElementName(this, element);
        Allure.step("На странице отображается элемент ".concat(elementName), () -> {
            element.shouldBe(visible);
        });
    }

    @Step("Страница содержит элемент с текстом: {text}")
    public void elementIsVisibleWithText(String text) {
        $x(String.format("//*[text()='%s']", text)).shouldBe(visible);
    }

    public void inputTextInElement(SelenideElement element, String value) {
        String elementName = ElementUtils.getElementName(this, element);

        Allure.step("Вводится значение '%s' в поле '%s'".formatted(value, elementName), () -> {
            element.shouldBe(visible).setValue(value);
        });
    }

    public String getAttributeValueStep(SelenideElement element, String attribute) {
        String elementName = ElementUtils.getElementName(this, element);
        return getAttributeValue(element, attribute, elementName);
    }

    public String getAttributeValueStep(SelenideElement element, String attribute, String elementName) {
        return getAttributeValue(element, attribute, elementName);
    }

    private String getAttributeValue(SelenideElement element, String attribute, String elementName) {
        return Allure.step("Получено значение атрибута '%s' у элемента '%s'".formatted(attribute, elementName), () -> {
            String value = element.getAttribute(attribute);
            if (value == null) value = "null";

            log.info("Получено значение атрибута '{}' у элемента '{}': {}", attribute, elementName, value);
            Allure.addAttachment("Value", value);

            return value;
        });
    }

    public void isNotEmptyListOfElements(ElementsCollection elementsCollection) {
        String elementName = ElementUtils.getElementsCollectionName(this, elementsCollection);
        Allure.step("Список элементов '%s' не пустой".formatted(elementName), () -> {
            elementsCollection.shouldHave(sizeGreaterThan(0));
        });
    }

    @Step("Получен список элементов: {elementName}")
    public ElementsCollection getElementsCollection(ElementsCollection elementsCollection, String elementName) {
        elementsCollection.shouldHave(sizeGreaterThan(0));
        return elementsCollection;
    }

    public List<String> getAttributeValuesFromCollection(ElementsCollection elementsCollection, String attribute) {
        String elementName = ElementUtils.getElementsCollectionName(this, elementsCollection);

        return Allure.step("Получен список значений атрибута '%s' у коллекции элементов '%s'"
                .formatted(attribute, elementName), () -> {
            elementsCollection.shouldHave(sizeGreaterThan(0));
            List<String> values = elementsCollection
                    .stream()
                    .map(element -> element.getAttribute(attribute))
                    .toList();
            log.info("Получен список: {}", values);
            Allure.addAttachment("List", values.toString());
            return values;
        });
    }

    @Step("Списки '{listName}' равны")
    public <T> void checkEqualityLists(List<T> actual, List<T> expected, String listName) {
        log.info("Получены списки: \n%s\n%s".formatted(actual, expected));
        Allure.addAttachment("expected list", expected.toString());
        Allure.addAttachment("actual list", actual.toString());
        assertThat(
                "Списки не равны",
                actual,
                equalTo(expected)
        );
    }

    protected SelenideElement getElementWithText(String text) {
        return $x(String.format("//*[text()='%s']", text));
    }

}
