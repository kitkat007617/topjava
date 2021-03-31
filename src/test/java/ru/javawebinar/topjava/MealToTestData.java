package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealToTestData {

    public static final TestMatcher<MealTo> MEALTO_MATCHER = TestMatcher.usingIgnoringFieldsComparator(MealTo.class, "dateTime", "excess");

    public static final int NOT_FOUND = 10;
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 9;

    public static final MealTo meal1 = new MealTo(MEAL1_ID, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false);
    public static final MealTo meal2 = new MealTo(MEAL1_ID + 1, of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, false);
    public static final MealTo meal3 = new MealTo(MEAL1_ID + 2, of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false);
    public static final MealTo meal4 = new MealTo(MEAL1_ID + 3, of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, false);
    public static final MealTo meal5 = new MealTo(MEAL1_ID + 4, of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500, false);
    public static final MealTo meal6 = new MealTo(MEAL1_ID + 5, of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000, false);
    public static final MealTo meal7 = new MealTo(MEAL1_ID + 6, of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 510, false);
    public static final MealTo adminMeal1 = new MealTo(ADMIN_MEAL_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510, false);
    public static final MealTo adminMeal2 = new MealTo(ADMIN_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500, false);

    public static final List<MealTo> meals = List.of(meal7, meal6, meal5, meal4, meal3, meal2, meal1);
}
