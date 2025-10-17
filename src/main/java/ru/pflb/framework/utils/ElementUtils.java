package ru.pflb.framework.utils;

import com.codeborne.selenide.SelenideElement;
import ru.pflb.framework.annotations.ElementName;

import java.lang.reflect.Field;

public class ElementUtils {

    public static String getElementName(Object pageObject, SelenideElement element) {
        if (pageObject == null || element == null) return "Неизвестный элемент";

        try {
            for (Field field : pageObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(pageObject);

                if (value instanceof SelenideElement selenideElement
                        && isSameElement(selenideElement, element)
                        && field.isAnnotationPresent(ElementName.class)) {

                    return field.getAnnotation(ElementName.class).value();
                }
            }
        } catch (Exception _) {
        }

        return "Неизвестный элемент";
    }

    private static boolean isSameElement(SelenideElement firstElement, SelenideElement secondElement) {
        try {
            return firstElement.toString().equals(secondElement.toString());
        } catch (Exception e) {
            return false;
        }
    }

}
