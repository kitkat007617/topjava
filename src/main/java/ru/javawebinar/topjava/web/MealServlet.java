package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.StoreMealsUtil;
import ru.javawebinar.topjava.util.StoreUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "MealServlet", value = "/MealServlet")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static volatile StoreUtil store = StoreMealsUtil.getInstance();
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEALS = "/meals.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        if (action == null){
            forward = LIST_MEALS;
            List<MealTo> mealsWithExcess = MealsUtil.filteredByStreams(store.getAllMeals(), null, null, 2000);
            request.setAttribute("mealsWithExcess", mealsWithExcess);
            log.debug("forward to meals");
        } else if (action.equalsIgnoreCase("get")) {
            forward = LIST_MEALS;
            List<MealTo> mealsWithExcess = MealsUtil.filteredByStreams(store.getAllMeals(), null, null, 2000);
            request.setAttribute("mealsWithExcess", mealsWithExcess);
            log.debug("forward to meals");
        } else if (action.equalsIgnoreCase("delete")){
            forward = LIST_MEALS;
            int mealId = Integer.parseInt(request.getParameter("id"));
            store.deleteMeal(mealId);
            List<MealTo> mealsWithExcess = MealsUtil.filteredByStreams(store.getAllMeals(), null, null, 2000);
            request.setAttribute("mealsWithExcess", mealsWithExcess);
            log.debug("forward to meals");
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            log.debug("forward to meal");
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = store.getMealById(mealId);
            request.setAttribute("meal", meal);
        }  else {
            forward = INSERT_OR_EDIT;
            log.debug("forward to meal");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dateAndTime = request.getParameter("dateAndTime");
        LocalDateTime localDateTime = parseLocalDateTime(dateAndTime);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()){
            store.creatMeal(localDateTime, description, calories);
        } else {
            Meal meal = new Meal(Integer.parseInt(id), localDateTime, description, calories);
            store.updateMeal(meal);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_MEALS);

        request.setAttribute("mealsWithExcess", MealsUtil.filteredByStreams(store.getAllMeals(), null, null, 2000));
        dispatcher.forward(request, response);
    }

    private LocalDateTime parseLocalDateTime(String dateAndTime){
        String[] dateAndTimeArray = dateAndTime.split(" ");
        String[] date = dateAndTimeArray[0].split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]));
        String[] time = dateAndTimeArray[1].split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        return LocalDateTime.of(localDate, localTime);
    }
}
