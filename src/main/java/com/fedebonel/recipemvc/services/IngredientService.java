package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.model.Recipe;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findCommandById(String recipeId, String IngredientId);

    Mono<IngredientCommand> saveCommand(IngredientCommand ingredientCommand);

    Mono<Recipe> deleteById(String recipeId, String ingredientId);
}
