package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            for (MealTo mealTo: mealRestController.getAll())
                System.out.println(mealTo);
            //mealRestController.delete(8);
            Meal meal = new Meal(8, LocalDateTime.of(1999, Month.MAY, 30, 0, 0), "Изменит ли?", 500);
            mealRestController.update(meal, 8);
            LocalDate startDate = LocalDate.of(2020, Month.JANUARY, 1);
            LocalDate endDate = LocalDate.of(2022, Month.MAY, 30);
            LocalTime startTime = LocalTime.of(10, 0, 0);
            LocalTime endTime = LocalTime.of(12, 10, 49);
            for (MealTo mealTo : mealRestController.getAll(null, null, null, null))
                System.out.println(mealTo);
        }
    }
}
