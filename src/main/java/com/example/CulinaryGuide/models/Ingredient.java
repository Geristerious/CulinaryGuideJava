package com.example.CulinaryGuide.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;


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
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, message = "Введите название категории")
    @Size(max = 25, message = "Максимальное количество символов 25")
    @Pattern(regexp = "^[а-яёА-ЯЁ\s]*$",message = "Используйте буквы принадлежащие русскому алфавиту")
    private String nameIngredient ;


    @ManyToOne()
    @JoinColumn(name = "ingredient_category_id", referencedColumnName = "id")

    private IngredientCategory ingredientCategory;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientDish> ingredientDishes =new ArrayList<>();


    @OneToMany(mappedBy = "ingredient")
    private List<IngredientReplacement> ingredientReplacements;



}
