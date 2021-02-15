package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));

        save(new Meal(LocalDateTime.of(1999, Month.MAY, 30, 0, 0), "Торт", 500), 1);
        save(new Meal(LocalDateTime.of(2021, Month.MAY, 30, 0, 0), "Еще торт", 500), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} userId {}", meal, userId);
        Map<Integer, Meal> mapEntity = new HashMap<>();
        mapEntity.put(userId, meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), mapEntity);
            return meal;
        }
        // handle case: update, but not present in storage
        else {
            if (!repository.get(meal.getId()).containsKey(userId))
                return null;
            repository.put(meal.getId(), mapEntity);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete id {} userId {}", id, userId);
        if (repository.get(id).containsKey(userId))
            return repository.remove(id) != null;
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id {} userId {}", id, userId);
        return repository.get(id).get(userId);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream().filter(map -> map.containsKey(userId)).map(map -> map.get(userId)).
                sorted((meal1, meal2) -> {
                    return meal2.getDateTime().compareTo(meal1.getDateTime());
                }).collect(Collectors.toList());
    }
}

