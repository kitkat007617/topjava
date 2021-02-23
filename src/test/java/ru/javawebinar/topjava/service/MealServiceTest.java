package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal created = mealService.get(MealTestData.MEAL_ID, UserTestData.USER_ID);
        MealTestData.assertMatch(created, MealTestData.meal1);
    }

    @Test
    public void getMealOfAnotherUser() {
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.MEAL_ID, 2));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.MEAL_ID, 2));
    }

    @Test
    public void delete() {
        mealService.delete(MealTestData.MEAL_ID, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.MEAL_ID,
                UserTestData.USER_ID));
    }

    @Test
    public void deleteNotFound(){
        assertThrows(NotFoundException.class, () -> mealService.delete(MealTestData.MEAL_ID, 2));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = mealService.getBetweenInclusive(LocalDate.of(2020, 01, 31),
                LocalDate.of(2020, 10, 21), UserTestData.USER_ID);
        List<Meal> expected = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = mealService.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(actual, MealTestData.meal1, MealTestData.meal2, MealTestData.meal3,
                MealTestData.meal4, MealTestData.meal5, MealTestData.meal6);
    }

    @Test
    public void update() {
        mealService.update(MealTestData.getUpdate(), UserTestData.USER_ID);
        MealTestData.assertMatch(mealService.get(MealTestData.getUpdate().getId(), UserTestData.USER_ID),
                MealTestData.getUpdate());
    }

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), UserTestData.USER_ID);
        Integer newId = created.getId();
        Meal expected = MealTestData.getNew();
        expected.setId(newId);
        MealTestData.assertMatch(created, expected);
        MealTestData.assertMatch(mealService.get(newId, UserTestData.USER_ID), expected);
    }
}