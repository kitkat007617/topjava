package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

public final class StoreMealsUtil implements StoreUtil{
    private static final Logger log = getLogger(StoreMealsUtil.class);
    private static volatile StoreMealsUtil instance;
    private static final ConcurrentMap<Integer, Meal> mealsStore = new ConcurrentHashMap<>();

    static {
        Meal meal1 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        Meal meal3 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        Meal meal4 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        Meal meal5 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        Meal meal6 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        Meal meal7 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        mealsStore.put(meal1.getId(), meal1);
        mealsStore.put(meal2.getId(), meal2);
        mealsStore.put(meal3.getId(), meal3);
        mealsStore.put(meal4.getId(), meal4);
        mealsStore.put(meal5.getId(), meal5);
        mealsStore.put(meal6.getId(), meal6);
        mealsStore.put(meal7.getId(), meal7);
    }

    private StoreMealsUtil(){
    }

    public static StoreMealsUtil getInstance(){
        StoreMealsUtil result = instance;
        if (result != null)
            return result;
        synchronized (StoreMealsUtil.class){
            if (instance == null)
                instance = new StoreMealsUtil();
        }
        return instance;
    }

    @Override
    public synchronized void creatMeal(LocalDateTime localDateTime, String description, int calories) {
        log.debug("Store meals util - create meal");
        Meal newMeal = new Meal(localDateTime, description, calories);
        mealsStore.put(newMeal.getId(), newMeal);
    }

    @Override
    public synchronized void deleteMeal(int id) {
        log.debug("Store meals util - delete meal");
        mealsStore.remove(id);
    }

    @Override
    public synchronized void updateMeal(Meal meal) {
        log.debug("Store meal util - update meal");
        mealsStore.put(meal.getId(), meal);

    }

    @Override
    public synchronized Meal getMealById(int id) {
        log.debug("Store meal util - get meal by id");
        return mealsStore.get(id);
    }

    @Override
    public synchronized List<Meal> getAllMeals() {
        log.debug("Store meal util - get all meals");
        return new ArrayList<>(mealsStore.values());
    }
}
