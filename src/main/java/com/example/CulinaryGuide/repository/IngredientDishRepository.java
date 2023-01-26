package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Cooking;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.models.IngredientDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientDishRepository extends JpaRepository<IngredientDish,Long> {
    List<IngredientDish> getIngredientDishByIngredient_Id(Long id);

    List<IngredientDish> getIngredientDishByCooking_Id(Long id);
}
