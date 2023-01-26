package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.Cooking;
import com.example.CulinaryGuide.models.CookingPhoto;
import com.example.CulinaryGuide.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CookingPhotosRepository extends JpaRepository<CookingPhoto,Long> {
    //List<IngredientCategory> findByLastName(String lastName);

    List<CookingPhoto> getCookingPhotoByCooking_Id(Long id);
}