package com.example.CulinaryGuide.models;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

import java.math.BigDecimal;
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
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 1, message = "Введите название категории")
    @Size(max = 25, message = "Максимальное количество символов 25")
    @Pattern(regexp = "^[а-яёА-ЯЁ\s]*$",message = "Используйте буквы принадлежащие русскому алфавиту")
    private String nameDish;

    @DecimalMax(value = "9999",message ="Количество калорий должно принадлежать интервалу от 0 до 9999" )
//    @DecimalMin(value = "1",message ="Количесетво калорий должно принадлежать интервалу от 1 до 9999")
    private BigDecimal valueCalories;
    @DecimalMax(value = "9999",message ="Количество жиров должно принадлежать интервалу от 0 до 9999" )
//    @DecimalMin(value = "1",message ="Количесетво жиров должно принадлежать интервалу от 1 до 9999")
    private BigDecimal valueFats;
    @DecimalMax(value = "9999",message ="Количество белков должно принадлежать интервалу от 0 до 9999" )
//    @DecimalMin(value = "1",message ="Количесетво белков должно принадлежать интервалу от 1 до 9999")
    private BigDecimal valueProteins;

    @DecimalMax(value = "9999",message ="Количество углеводов должно принадлежать интервалу от 1 до 9999" )
//    @DecimalMin(value = "1",message ="Количесетво углеводов должно принадлежать интервалу от 1 до 9999")
    private BigDecimal valueCarbohydrates;
    private String author;

    @ManyToMany(cascade ={CascadeType.PERSIST,CascadeType.MERGE} )
    @ToString.Exclude
    @JoinTable(
            name = "dish_dish_category",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_category_id"))

    private Collection<DishCategory> dishCategories =new ArrayList<>();

    @OneToMany(mappedBy = "dish")
    @ToString.Exclude
    private Collection<Cooking> cookings =new ArrayList<>();;



    public Dish(Long id, String nameDish) {
        this.id = id;
        this.nameDish = nameDish;
    }
}
