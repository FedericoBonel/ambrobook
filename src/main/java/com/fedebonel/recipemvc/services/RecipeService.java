package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.model.Recipe;

import java.util.List;
import java.util.Set;

/**
 * Recipes service
 */
public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    void deleteById(Long id);

    RecipeDto findCommandById(Long id);

    RecipeDto saveRecipeCommand(RecipeDto recipeDto);

    List<Recipe> findByQuery(String query);
}
