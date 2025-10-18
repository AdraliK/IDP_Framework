package ru.pflb.framework.pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pflb.framework.utils.ElementUtils;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public abstract class BasePage {

    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    @Step("Открываю страницу: {url}")
    public void openPage(String url) {
        open(url);
    }

    @Step("Название странцы: {titleName}")
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

        Allure.step("Вводится значение '%s' в поле %s".formatted(value, elementName), () -> {
            element.shouldBe(visible).setValue(value);
        });
    }

    public String getAttributeValue(SelenideElement element, String attribute) {
        String elementName = ElementUtils.getElementName(this, element);

        String value = element.getAttribute(attribute);
        if (value == null) value = "null";

        log.info("Получено значение атрибута '{}' у элемента {}: {}", attribute, elementName, value);
        Allure.step("Получено значение атрибута '%s' у элемента %s".formatted(attribute, elementName), () -> {});
        Allure.addAttachment("Value", value);

        return value;
    }

    protected SelenideElement getElementWithText(String text) {
        return $x(String.format("//*[text()='%s']", text));
    }

}
