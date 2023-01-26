package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.SelectedRecipes;
import com.example.CulinaryGuide.repository.SelectedRecipesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SelectedRecipesService {
    private final SelectedRecipesRepository selectedRecipesRepository;
    public SelectedRecipesService(SelectedRecipesRepository selectedRecipesRepository) {
        this.selectedRecipesRepository = selectedRecipesRepository;
    }

    public List<SelectedRecipes> list(){
        return selectedRecipesRepository.findAll();
    }
    public void deleteSelectedRecipe(Long id){
        selectedRecipesRepository.deleteById(id);
    }
    public void postSelectedRecipe(SelectedRecipes selectedRecipes){
        selectedRecipesRepository.save(selectedRecipes);
    }


//    public List<SelectedRecipes> getSelectedRecipesByAuthor(String author){
//       return selectedRecipesRepository.getSelectedRecipesByAuthor(author);
//    }


//    public void putDishCategory(Long id, DishCategory dishCategory){
////        return (dishCategoriesRepository.existsById(id))?
////                new ResponseEntity<>(dishCategoriesRepository.save(dishCategory),
////                        HttpStatus.OK):
////                new ResponseEntity<>(studentRepository.save(student),
////                        HttpStatus.CREATED);
//    }

}
