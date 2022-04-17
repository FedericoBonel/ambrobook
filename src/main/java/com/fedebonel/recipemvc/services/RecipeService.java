package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.Recipe;

import java.util.Set;

/**
 * Recipes service
 */
public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long id);
}
