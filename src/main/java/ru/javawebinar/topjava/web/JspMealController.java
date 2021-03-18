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
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    @PostMapping("/meals")
    public String create(@RequestParam("description") String description,
                         @RequestParam("dateTime") String dateTime,
                         @RequestParam("calories") String calories){
        Meal newMeal = new Meal(LocalDateTime.parse(dateTime), description,
                Integer.parseInt(calories));
        service.create(newMeal, SecurityUtil.authUserId());

        return "redirect:meals";
    }

    @PostMapping("/meals")
    public String update(@RequestParam("id") String id,
                         @RequestParam("description") String description,
                         @RequestParam("dateTime") String dateTime,
                         @RequestParam("calories") String calories) {
        Meal updatedMeal = new Meal(Integer.parseInt(id),
                                    LocalDateTime.parse(dateTime),
                                    description,
                                    Integer.parseInt(calories));
        service.update(updatedMeal, SecurityUtil.authUserId());
        return "redirect:meals";
    }

    @PostMapping("/meals")
    public String delete(@RequestParam("id") String id) {
        service.delete(Integer.parseInt(id), SecurityUtil.authUserId());
        return "redirect:meals";
    }

    @GetMapping("/meals")
    public String filter(@RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime, Model model) {

        LocalDate localDateStart = LocalDate.parse(startDate);
        LocalDate localDateEnd = LocalDate.parse(endDate);
        LocalTime localTimeStart = LocalTime.parse(startTime);
        LocalTime localTimeEnd = LocalTime.parse(endTime);

        List<Meal> mealsDateFilter = service.getBetweenInclusive(localDateStart, localDateEnd, SecurityUtil.authUserId());
        List<MealTo> mealsToTimeFilter = MealsUtil.getFilteredTos(mealsDateFilter, SecurityUtil.authUserCaloriesPerDay(),
                localTimeStart, localTimeEnd);
        model.addAttribute("meals", mealsToTimeFilter);
        return "meals";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        model.addAttribute("meals", service.getAll(SecurityUtil.authUserId()));
        return "meals";
    }


}
