package com.example.CulinaryGuide.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class Cooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timeCooking;
    @Size(max = 5000, message = "Максимальное количество символов 5000")
    private String descriptionCooking;
    private String author ;
    @ManyToOne
    private Dish dish;
    @OneToMany(mappedBy = "cooking")

    private Collection<IngredientDish> ingredientDishes=new ArrayList<IngredientDish>();

    @OneToMany(mappedBy = "cooking")

    private List<IngredientReplacement> ingredientReplacements;


//    @ManyToOne()
//    @JoinColumn(name = "ingredient_category_id", referencedColumnName = "id")
    @OneToMany(mappedBy = "cooking")
    private List<SelectedRecipes> selectedRecipes;

    @OneToMany(mappedBy = "cooking")

    private List<CookingPhoto> cookingPhotos;

}
