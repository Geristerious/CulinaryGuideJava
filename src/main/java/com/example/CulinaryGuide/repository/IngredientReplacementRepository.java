package com.example.CulinaryGuide.repository;

import com.example.CulinaryGuide.models.IngredientDish;
import com.example.CulinaryGuide.models.IngredientReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientReplacementRepository extends JpaRepository<IngredientReplacement,Long> {
    List<IngredientReplacement> getIngredientReplacementByIngredientRep_id(Long id);
    List<IngredientReplacement> getIngredientReplacementByCooking_Id(Long id);
}
