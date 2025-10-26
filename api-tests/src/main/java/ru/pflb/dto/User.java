package ru.pflb.dto;

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