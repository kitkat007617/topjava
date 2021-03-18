package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;

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

}
