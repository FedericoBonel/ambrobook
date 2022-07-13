package com.fedebonel.recipemvc.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;

    @Column(length = 64)
    private String verificationCode;

    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    @ManyToMany
    @JoinTable(name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> likedRecipes = new ArrayList<>();


    public void likeRecipe(Recipe recipe) {
        if (likedRecipes.stream()
                .noneMatch(savedRecipe -> recipe.getId().equals(savedRecipe.getId()))) {
            likedRecipes.add(recipe);
        } else {
            likedRecipes.removeIf(savedRecipe -> recipe.getId().equals(savedRecipe.getId()));
        }
    }
}
