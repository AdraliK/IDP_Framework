package ru.pflb.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pflb.utils.Operator;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CommonSteps {

    protected static final Logger log = LoggerFactory.getLogger(CommonSteps.class);

    @Step("Значение '{actualValue}' {operator} '{expectedValue}'")
    public static <T extends Comparable<T>> void checkComparison(T actualValue, T expectedValue, Operator operator) {
        switch (operator) {
            case EQUALS -> assertThat(
                    "Ожидалось, что значение будет равно " + expectedValue + ", но получили " + actualValue,
                    actualValue,
                    equalTo(expectedValue)
            );
            case NOT_EQUALS -> assertThat(
                    "Ожидалось, что значение не будет равно " + expectedValue + ", но получили " + actualValue,
                    actualValue,
                    not(equalTo(expectedValue))
            );
            case GREATER -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет больше чем " + expectedValue,
                    actualValue.compareTo(expectedValue) > 0
            );
            case LESS -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет меньше чем " + expectedValue,
                    actualValue.compareTo(expectedValue) < 0
            );
            case GREATER_OR_EQUAL -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет больше или равно " + expectedValue,
                    actualValue.compareTo(expectedValue) >= 0
            );
            case LESS_OR_EQUAL -> assertThat(
                    "Ожидалось, что значение " + actualValue + " будет меньше или равно " + expectedValue,
                    actualValue.compareTo(expectedValue) <= 0
            );
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }

    @Step("Списки '{listName}' равны")
    public static <T> void checkEqualityLists(List<T> actual, List<T> expected, String listName) {
        log.info("Получены списки: \n%s\n%s".formatted(actual, expected));
        Allure.addAttachment("expected list", expected.toString());
        Allure.addAttachment("actual list", actual.toString());
        assertThat(
                "Списки не равны",
                actual,
                equalTo(expected)
        );
    }

}
