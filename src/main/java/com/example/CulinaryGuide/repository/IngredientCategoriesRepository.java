package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.models.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientCategoriesRepository extends JpaRepository<IngredientCategory,Long> {
    boolean existsIngredientCategoryByNameIngredientCategory(String nameIngredientCategory);
    IngredientCategory getIngredientCategoryByNameIngredientCategory(String nameIngredientCategory);

}
