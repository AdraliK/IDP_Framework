package ru.pflb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Car {
    String engineType;
    int id;
    String mark;
    String model;
    BigDecimal price;
}

