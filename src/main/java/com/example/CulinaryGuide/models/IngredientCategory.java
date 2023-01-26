package com.example.CulinaryGuide.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class IngredientCategory {//+
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, message = "Введите название категории")
    @Size(max = 20, message = "Максимальное количество символов 20")
    @Pattern(regexp = "^[а-яёА-ЯЁ\s]*$",message = "Используйте буквы принадлежащие русскому алфавиту")
    private String nameIngredientCategory;

    @OneToMany(mappedBy = "ingredientCategory")
    private List<Ingredient> ingredients;

}
