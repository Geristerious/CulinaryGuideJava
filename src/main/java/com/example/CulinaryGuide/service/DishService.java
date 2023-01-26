package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Dish;
import com.example.CulinaryGuide.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    private final DishRepository dishesRepository;

    public Optional<Dish> getDishById(Long id){return dishesRepository.findById(id);}
    public DishService(DishRepository dishesRepository) {
        this.dishesRepository = dishesRepository;
    }

    public List<Dish> list(){
        return dishesRepository.findAll();
    }
    public List<Dish> getDishByAuthor(String author){
        return dishesRepository.getDishByAuthor(author);
    }
    public void deleteDish(Long id){
        dishesRepository.deleteById(id);
    }
    public void postDish(Dish dish){
        dishesRepository.save(dish);
    }

    public Dish getDishByNameDish(String nameDish){
       return dishesRepository.getDishByNameDish(nameDish);
    }
    public boolean checkNameDish(String nameDish){
        return  dishesRepository.existsDishByNameDish(nameDish);
    }
   // public boolean checkIngredientCategory(Long id){return  ingredientsRepository.existsIngredientByIngredientCategory_Id(id);}

}
