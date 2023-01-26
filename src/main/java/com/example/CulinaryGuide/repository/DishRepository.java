package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish,Long> {
    //List<IngredientCategory> findByLastName(String lastName);
    //boolean existsDishByIngredientCategory_Id(Long id);
    Dish getDishByNameDish(String nameDish);
    List<Dish> getDishByAuthor(String nameDish);
    boolean existsDishByNameDish(String nameDish);
}