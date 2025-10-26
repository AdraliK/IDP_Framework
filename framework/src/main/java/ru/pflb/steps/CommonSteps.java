package ru.pflb.steps;

import io.qameta.allure.Step;
import ru.pflb.utils.Operator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CommonSteps {

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

}
