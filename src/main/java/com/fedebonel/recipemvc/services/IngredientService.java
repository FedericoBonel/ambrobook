package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findCommandById(String recipeId, String IngredientId);

    IngredientCommand saveCommand(IngredientCommand ingredientCommand);

    void deleteById(String recipeId, String ingredientId);
}
