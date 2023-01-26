package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.Cooking;
import com.example.CulinaryGuide.models.Dish;
import com.example.CulinaryGuide.repository.CookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CookingService {
    private final CookingRepository cookingRepository;
    public CookingService(CookingRepository cookingRepository) {
        this.cookingRepository = cookingRepository;
    }

    public List<Cooking> list(){
        return cookingRepository.findAll();
    }
    public void deleteCooking(Long id){
        cookingRepository.deleteById(id);
    }
    public void postCooking(Cooking cooking){
        cookingRepository.save(cooking);
    }

    public boolean checkCooking(Long id){return  cookingRepository.existsCookingByDish_Id(id);}

    public Optional<Cooking> getCookingById(Long id){return cookingRepository.findById(id);}

    public List<Cooking> getCookingByDishId(Long id){
        return cookingRepository.getCookingByDish_Id(id) ;
    }
}
