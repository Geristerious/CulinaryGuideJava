package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngredientsRepository extends JpaRepository<Ingredient,Long> {

    boolean existsIngredientByIngredientCategory_Id(Long id);
    boolean existsIngredientByNameIngredient(String nameIngredient);
    Ingredient getIngredientByNameIngredient(String nameIngredient);

    List<Ingredient> getIngredientByIngredientCategory_Id(Long id);
}