package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.DishCategory;
import com.example.CulinaryGuide.models.IngredientCategory;
import com.example.CulinaryGuide.repository.DishCategoriesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishCategoryService {
    private final DishCategoriesRepository dishCategoriesRepository;
    public DishCategoryService(DishCategoriesRepository dishCategoriesRepository) {
        this.dishCategoriesRepository = dishCategoriesRepository;
    }

    public List<DishCategory> list(){
        return dishCategoriesRepository.findAll();
    }
    public void deleteDishCategory(Long id){
        dishCategoriesRepository.deleteById(id);
    }
    public void postDishCategory(DishCategory dishCategory){
         dishCategoriesRepository.save(dishCategory);
    }

    public Optional<DishCategory> getDishCategoryById(Long id){ return dishCategoriesRepository.findById(id); }


    public boolean checkNameDishCategory(String nameDishCategory){
        return  dishCategoriesRepository.existsDishCategoryByNameDishCategory(nameDishCategory);
    }

    public DishCategory getDishCategoryByNameDishCategory(String nameDishCategory){
        return  dishCategoriesRepository.getDishCategoryByNameDishCategory(nameDishCategory);
    }
}
