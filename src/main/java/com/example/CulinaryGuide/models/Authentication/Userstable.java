package com.example.CulinaryGuide.models.Authentication;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="userstable")
public class Userstable {


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "email")
    private String email;
    @Column(name = "enabled")
    private Long enabled;

    private String passwordConfirm;

//    @ManyToOne()
//    @JoinColumn(name = "ingredient_category_id", referencedColumnName = "id")
//    @NotNull(message = "Выберите категорию")
//    private IngredientCategory ingredientCategory;







}
