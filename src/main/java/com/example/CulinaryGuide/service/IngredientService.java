package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Ingredient;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.repository.IngredientsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    private final IngredientsRepository ingredientsRepository;
    public IngredientService(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    public boolean checkNameIngredient(String nameIngredient){
        return  ingredientsRepository.existsIngredientByNameIngredient(nameIngredient);
    }
    public Long countIngredientDishes(Long id){
        return ingredientsRepository.findById(id).get().getIngredientDishes().stream().count();
    }
    public Long countIngredientReplacement(Long id){
        return ingredientsRepository.findById(id).get().getIngredientReplacements().stream().count();
    }

    public List<Ingredient> list(){
        return ingredientsRepository.findAll();
    }
    public void deleteIngredient(Long id){
        ingredientsRepository.deleteById(id);
    }
    public void postIngredient(Ingredient ingredient){
        ingredientsRepository.save(ingredient);
    }
    public boolean checkIngredientCategory(Long id){return  ingredientsRepository.existsIngredientByIngredientCategory_Id(id);}
    public Optional<Ingredient> getIngredientById(Long id){return ingredientsRepository.findById(id);}

    public Ingredient getIngredientByNameIngredient(String nameIngredient){return  ingredientsRepository.getIngredientByNameIngredient(nameIngredient);}
    public List<Ingredient> getIngredientByIngredientCategory_Id(Long id){return  ingredientsRepository.getIngredientByIngredientCategory_Id(id);}

//    public void putIngredientCategory(Long id, DishCategory dishCategory){
//        return (ingredientCategoriesRepository.existsById(id))?
//                new ResponseEntity<>(dishCategoriesRepository.save(dishCategory),
//                        HttpStatus.OK):
//                new ResponseEntity<>(studentRepository.save(student),
//                        HttpStatus.CREATED);
//    }

}
