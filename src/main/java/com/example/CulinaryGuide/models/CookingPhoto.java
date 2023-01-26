package com.example.CulinaryGuide.models;
import javax.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class CookingPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public CookingPhoto(String filename) {
        filename = filename;
    }

    @ManyToOne
    @JoinColumn(name = "cooking_id")
    private Cooking cooking;
    private String filename;
}
