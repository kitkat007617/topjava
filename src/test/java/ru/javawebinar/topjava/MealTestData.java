package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL_ID = AbstractBaseEntity.START_SEQ + 2;
    public static final int NOT_FOUND = 1;

    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(expected).usingRecursiveComparison().ignoringFields("id").isEqualTo(actual);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected){
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected){
        assertThat(actual).usingElementComparatorIgnoringFields("id").isEqualTo(expected);
    }

    public static Meal getNew(){
        Meal newMeal = new Meal();
        newMeal.setId(null);
        newMeal.setCalories(500);
        newMeal.setDescription("Test");
        newMeal.setDateTime(LocalDateTime.MAX);
        return newMeal;
    }

    public static Meal getUpdate(){
        Meal updated = meals.get(0);
        updated.setId(MEAL_ID);
        updated.setDateTime(LocalDateTime.MIN);
        updated.setDescription("Updated");
        updated.setCalories(1000);
        return updated;
    }
}
