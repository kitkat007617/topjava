package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {
    private final LocalDateTime dateTime;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    private static int count = 0;

    private final String description;

    private final int calories;

    public int getId() {
        return id;
    }

    public Meal(int id, LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.id = id;
        this.description = description;
        this.calories = calories;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        count++;
        this.id = count;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
