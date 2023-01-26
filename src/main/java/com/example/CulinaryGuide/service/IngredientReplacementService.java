package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.IngredientDish;
import com.example.CulinaryGuide.models.IngredientReplacement;
import com.example.CulinaryGuide.repository.IngredientReplacementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientReplacementService {

    private final IngredientReplacementRepository ingredientReplacementRepository;

    public IngredientReplacementService(IngredientReplacementRepository ingredientReplacementRepository) {
        this.ingredientReplacementRepository = ingredientReplacementRepository;
    }
    public List<IngredientReplacement> getCookingByIngredient(Long id){
        return ingredientReplacementRepository.getIngredientReplacementByIngredientRep_id(id);
        //List<Cooking> getCookingByIngredient_Id(Long id);
    }
    public void postIngredientReplacement(IngredientReplacement ingredientReplacement){
        ingredientReplacementRepository.save(ingredientReplacement);
    }

    public List<IngredientReplacement> getIngredientReplacementByCooking_Id(Long id){
        return ingredientReplacementRepository.getIngredientReplacementByCooking_Id(id);
        //List<Cooking> getCookingByIngredient_Id(Long id);
    }

    public void deleteIngredientIngredientReplacement(Long id){
        ingredientReplacementRepository.deleteById(id);
    }
}
