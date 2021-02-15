package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public void delete(int id){
        log.info("delete {} userId {}", id, authUserId());
        service.delete(id, authUserId());
    }

    public List<MealTo> getAll(){
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime){
        log.info("getAll {} {} {} {} userId {}", startDate, startTime, endDate, endTime, authUserId());
        if (startDate == null)
            startDate = LocalDate.MIN;
        if (startTime == null)
            startTime = LocalTime.MIN;
        if (endDate == null)
            endDate = LocalDate.MAX;
        if (endTime == null)
            endTime = LocalTime.MAX;
        return MealsUtil.getFilteredTos(service.getAll(authUserId()), authUserCaloriesPerDay(), startDate,
                startTime, endDate, endTime);
    }

    public MealTo get(int id){
        log.info("get {} userId {}", id, authUserId());
        Optional<MealTo> meal = getAll().stream().filter(mealTo -> mealTo.getId() == id).findAny();
        if (meal.isPresent())
            return meal.get();
        else {
            throw new NotFoundException("Not found meal with id=" + id);
        }
    }

    public MealTo create(Meal meal){
        log.info("create {} userId {}", meal, authUserId());
        checkNew(meal);
        Meal meal1 = service.create(meal, authUserId());
        return getAll().stream().filter(mt -> mt.getId().equals(meal1.getId())).findAny().get();
    }

    public void update(Meal meal, int id){
        log.info("update {} with id {} and with userId {}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

}