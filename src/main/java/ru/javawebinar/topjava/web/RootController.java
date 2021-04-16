package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;

@Controller
public class RootController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/meals")
    public String getMeals() {
        return "meals";
    }
}
