package com.fedebonel.recipemvc.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Category POJO
 */
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

}
