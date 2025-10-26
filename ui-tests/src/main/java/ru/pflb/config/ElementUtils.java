package ru.pflb.config;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.pflb.annotations.ElementName;

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

        return element.toString();
    }

    private static boolean isSameElement(SelenideElement firstElement, SelenideElement secondElement) {
        try {
            return firstElement.toString().equals(secondElement.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getElementsCollectionName(Object pageObject, ElementsCollection elementsCollection) {
        if (pageObject == null || elementsCollection == null) return "Неизвестный элемент";

        try {
            for (Field field : pageObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(pageObject);

                if (value instanceof ElementsCollection collection
                        && isSameCollection(collection, elementsCollection)
                        && field.isAnnotationPresent(ElementName.class)) {

                    return field.getAnnotation(ElementName.class).value();
                }
            }
        } catch (Exception _) {
        }

        return elementsCollection.toString();
    }

    private static boolean isSameCollection(ElementsCollection firstCollection, ElementsCollection secondCollection) {
        try {
            return firstCollection.toString().equals(secondCollection.toString());
        } catch (Exception e) {
            return false;
        }
    }

}
