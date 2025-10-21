package ru.pflb.framework.dto.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {
    int age;
    String firstName;
    int id;
    BigDecimal money;
    String secondName;
    String sex;
}