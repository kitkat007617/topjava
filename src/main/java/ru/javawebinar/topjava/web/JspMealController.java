package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    @PostMapping("/mealCreateOrUpdate")
    public String createOrUpdate(@RequestParam("id") String id,
                                 @RequestParam("description") String description,
                                 @RequestParam("dateTime") String dateTime,
                                 @RequestParam("calories") String calories){
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description,
                Integer.parseInt(calories));

        if (id.length() != 0) {
            meal.setId(Integer.parseInt(id));
            service.update(meal, SecurityUtil.authUserId());
        } else
            service.create(meal, SecurityUtil.authUserId());

        return "redirect:meals";
    }
    @GetMapping("/mealForm")
    public String getMealForm(@RequestParam("id") String id, @RequestParam("action") String action, Model model) {
        Meal meal = "create".equals(action) ? new Meal(LocalDateTime.now(), "", 1000)
                : service.get(Integer.parseInt(id), SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping ("/mealDelete")
    public String delete(@RequestParam("id") String id) {
        service.delete(Integer.parseInt(id), SecurityUtil.authUserId());
        return "redirect:meals";
    }

    @GetMapping("/mealsFilter")
    public String filter(@RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime, Model model) {

        LocalDate localDateStart = DateTimeUtil.parseLocalDate(startDate);
        LocalDate localDateEnd = DateTimeUtil.parseLocalDate(endDate);
        LocalTime localTimeStart = DateTimeUtil.parseLocalTime(startTime);
        LocalTime localTimeEnd = DateTimeUtil.parseLocalTime(endTime);

        List<Meal> mealsDateFilter = service.getBetweenInclusive(localDateStart, localDateEnd, SecurityUtil.authUserId());
        List<MealTo> mealsToTimeFilter = MealsUtil.getFilteredTos(mealsDateFilter, SecurityUtil.authUserCaloriesPerDay(),
                localTimeStart, localTimeEnd);
        model.addAttribute("meals", mealsToTimeFilter);
        return "meals";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }
}
