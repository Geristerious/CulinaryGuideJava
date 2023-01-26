package com.example.CulinaryGuide.service;

import com.example.CulinaryGuide.models.CookingPhoto;
import com.example.CulinaryGuide.repository.CookingPhotosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CookingPhotoService {
    private final CookingPhotosRepository cookingPhotosRepository;
    public CookingPhotoService(CookingPhotosRepository cookingPhotosRepository) {
        this.cookingPhotosRepository = cookingPhotosRepository;
    }

    public List<CookingPhoto> list(){
        return cookingPhotosRepository.findAll();
    }
    public void deleteCookingPhoto(Long id){
        cookingPhotosRepository.deleteById(id);
    }
    public void postCookingPhoto(CookingPhoto cookingPhoto){
        cookingPhotosRepository.save(cookingPhoto);
    }

    public  List<CookingPhoto> getPhotoById(Long id){
        return  cookingPhotosRepository.getCookingPhotoByCooking_Id(id);
    }
//    public void putDishCategory(Long id, DishCategory dishCategory){
////        return (dishCategoriesRepository.existsById(id))?
////                new ResponseEntity<>(dishCategoriesRepository.save(dishCategory),
////                        HttpStatus.OK):
////                new ResponseEntity<>(studentRepository.save(student),
////                        HttpStatus.CREATED);
//    }

}
