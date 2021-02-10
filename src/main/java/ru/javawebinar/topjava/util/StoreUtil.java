package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreUtil {
    void creatMeal(LocalDateTime localDateTime, String description, int calories);
    void deleteMeal(int id);
    void updateMeal(Meal meal);
    Meal getMealById(int id);
    List<Meal> getAllMeals();
}
