package org.green_building.excellent_training.entities;

@lombok.Data
@lombok.AllArgsConstructor
public class Cat {

    private String name;
    private int age;
    private double salary;
    private Cat parent;
}
