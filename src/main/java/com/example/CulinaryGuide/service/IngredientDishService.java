package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Cooking;
import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.models.IngredientDish;
import com.example.CulinaryGuide.repository.IngredientDishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientDishService {

    private final IngredientDishRepository ingredientDishRepository;

    public IngredientDishService(IngredientDishRepository ingredientDishRepository) {
        this.ingredientDishRepository = ingredientDishRepository;
    }

    public void postIngredientDish(IngredientDish ingredientDish){
        ingredientDishRepository.save(ingredientDish);
    }
    public List<IngredientDish> getCookingByIngredient(Long id){
        return ingredientDishRepository.getIngredientDishByIngredient_Id(id);
        //List<Cooking> getCookingByIngredient_Id(Long id);
    }


    public List<IngredientDish> getIngredientDishByCooking_Id(Long id){
        return ingredientDishRepository.getIngredientDishByCooking_Id(id);
        //List<Cooking> getCookingByIngredient_Id(Long id);
    }

    public void deleteIngredientDish(Long id){
        ingredientDishRepository.deleteById(id);
    }
}
