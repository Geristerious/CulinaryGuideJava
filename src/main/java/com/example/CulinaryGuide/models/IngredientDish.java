package com.example.CulinaryGuide.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class IngredientDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    @ManyToOne
    @JoinColumn(name = "cooking_id")
    private Cooking cooking;
    private BigDecimal valueIngredient;
    private String unitMeasure;


    public IngredientDish(Ingredient ingredient, BigDecimal valueIngredient, String unitMeasure) {
        this.ingredient = ingredient;
        this.valueIngredient = valueIngredient;
        this.unitMeasure = unitMeasure;
    }
}
