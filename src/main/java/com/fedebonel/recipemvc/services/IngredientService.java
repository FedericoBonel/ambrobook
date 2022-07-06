package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;

public interface IngredientService {
    IngredientDto findCommandById(Long recipeId, Long IngredientId);

    IngredientDto saveCommand(IngredientDto ingredientDto);

    void deleteById(Long recipeId, Long ingredientId);
}
