package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> filteredListOfUserMealByTime = new ArrayList<>();
        List<UserMealWithExcess> finalFilteredListByTime = new ArrayList<>();
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        for (UserMeal um : meals) {
            LocalDateTime dateTimeForCurrentMeal = um.getDateTime();
            LocalDate dateForCurrentMeal = dateTimeForCurrentMeal.toLocalDate();
            LocalTime timeForCurrentMeal = dateTimeForCurrentMeal.toLocalTime();
            caloriesByDays.computeIfPresent(dateForCurrentMeal, (key, value) -> value + um.getCalories());
            caloriesByDays.computeIfAbsent(dateForCurrentMeal, (key) -> um.getCalories());
            if (TimeUtil.isBetweenHalfOpen(timeForCurrentMeal, startTime, endTime))
                filteredListOfUserMealByTime.add(um);
        }
        for (UserMeal um : filteredListOfUserMealByTime) {
            LocalDate dateForCurrentMeal = um.getDateTime().toLocalDate();
            boolean excess = false;
            if (caloriesByDays.get(dateForCurrentMeal) > caloriesPerDay)
                excess = true;
            finalFilteredListByTime.add(new UserMealWithExcess(um.getDateTime(), um.getDescription(), um.getCalories(), excess));
        }
        return finalFilteredListByTime;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        List<UserMeal> listOfMeals = meals.stream().map((um) -> {
            caloriesByDays.computeIfAbsent(um.getDateTime().toLocalDate(), (key) -> um.getCalories());
            caloriesByDays.computeIfPresent(um.getDateTime().toLocalDate(), (key, value) -> value + um.getCalories());
            return um;
        }).filter((um) -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(), startTime, endTime)).collect(Collectors.toList());
        return listOfMeals.stream().map((us) -> {
            boolean excess = false;
            if (caloriesByDays.get(us.getDateTime().toLocalDate()) > caloriesPerDay)
                excess = true;
            return new UserMealWithExcess(us.getDateTime(), us.getDescription(), us.getCalories(), excess);
        }).collect(Collectors.toList());
    }
}
