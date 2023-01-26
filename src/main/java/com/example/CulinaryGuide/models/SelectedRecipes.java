package com.example.CulinaryGuide.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class SelectedRecipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Author ;


    @ManyToOne()
    @JoinColumn(name = "cooking_id", referencedColumnName = "id")
    private Cooking cooking;

}
