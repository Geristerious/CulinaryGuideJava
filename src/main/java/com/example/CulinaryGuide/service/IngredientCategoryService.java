package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.repository.DishCategoriesRepository;
import com.example.CulinaryGuide.repository.IngredientCategoriesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientCategoryService {
    private final IngredientCategoriesRepository ingredientCategoriesRepository;
    public IngredientCategoryService(IngredientCategoriesRepository ingredientCategoriesRepository) {
        this.ingredientCategoriesRepository = ingredientCategoriesRepository;
    }


    public List<IngredientCategory> list(){
        return ingredientCategoriesRepository.findAll();
    }

    public Optional<IngredientCategory> getIngredientCategoryById(Long id){return ingredientCategoriesRepository.findById(id);}

    public void deleteIngredientCategory(Long id){
        ingredientCategoriesRepository.deleteById(id);
    }

    public void postIngredientCategory(IngredientCategory ingredientCategory){
        ingredientCategoriesRepository.save(ingredientCategory);
    }

    public void putIngredientCategory(IngredientCategory ingredientCategory){
        ingredientCategoriesRepository.save(ingredientCategory);
    }


    public boolean checkNameIngredientCategory(String nameIngredientCategory){
        return  ingredientCategoriesRepository.existsIngredientCategoryByNameIngredientCategory(nameIngredientCategory);
    }

    public IngredientCategory getIngredientCategoryByNameIngredientCategory(String nameIngredientCategory){
        return  ingredientCategoriesRepository.getIngredientCategoryByNameIngredientCategory(nameIngredientCategory);
    }


    // IngredientCategory findByNameIngredientCategory(String nameIngredientCategory);

    //boolean existsIngredientCategoryByNameIngredientCategoryAndId(String nameIngredientCategory);

//    public void putIngredientCategory(Long id, DishCategory dishCategory){
//        return (ingredientCategoriesRepository.existsById(id))?
//                new ResponseEntity<>(dishCategoriesRepository.save(dishCategory),
//                        HttpStatus.OK):
//                new ResponseEntity<>(studentRepository.save(student),
//                        HttpStatus.CREATED);
//    }

}
