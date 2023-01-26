package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.models.IngredientReplacement;
import com.example.CulinaryGuide.models.SelectedRecipes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectedRecipesRepository extends JpaRepository<SelectedRecipes,Long> {
    //List<SelectedRecipes> getSelectedRecipesByAuthor(String author);
    //List<SelectedRecipes> getSelectedRecipesByAuthor(String author);
   // List<Ingredient> getIngredientByIngredientCategory_Id(Long id);
}