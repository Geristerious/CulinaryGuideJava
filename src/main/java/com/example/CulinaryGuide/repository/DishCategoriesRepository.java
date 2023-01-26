package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishCategoriesRepository extends JpaRepository<DishCategory,Long> {
    //List<IngredientCategory> findByLastName(String lastName);
    boolean existsDishCategoryByNameDishCategory(String nameDishCategory);
    DishCategory getDishCategoryByNameDishCategory(String nameDishCategory);
}