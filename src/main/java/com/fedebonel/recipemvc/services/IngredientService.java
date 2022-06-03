package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findCommandById(Long recipeId, Long IngredientId);

    IngredientCommand saveCommand(IngredientCommand ingredientCommand);

    void deleteById(Long recipeId, Long ingredientId);
}
