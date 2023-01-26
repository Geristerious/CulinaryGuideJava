package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Cooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CookingRepository extends JpaRepository<Cooking,Long> {
    //List<IngredientCategory> findByLastName(String lastName);
    boolean existsCookingByDish_Id(Long id);

    List<Cooking> getCookingByDish_Id(Long id);
}