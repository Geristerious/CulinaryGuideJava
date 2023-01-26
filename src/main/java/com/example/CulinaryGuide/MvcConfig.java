package com.example.CulinaryGuide;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry
                                           registry) {
        registry.addViewController("/cooking").setViewName("Cooking/index");
        registry.addViewController("/dishCategory").setViewName("DishCategory/index");
        registry.addViewController("/dishCategory").setViewName("DishCategory/index");
        registry.addViewController("/ingredientCategory").setViewName("IngredientCategory/index1");
        registry.addViewController("/ingredient").setViewName("Ingredient/index");
        registry.addViewController("/dish").setViewName("Dish/index");
       // registry.addViewController("/ingredient").setViewName("ingredient");
        //New/home
        //registry.addViewController("/").setViewName("home");
        //registry.addViewController("/main").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
    }
}
