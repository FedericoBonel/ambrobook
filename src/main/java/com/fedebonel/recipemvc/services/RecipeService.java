package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Recipes service
 */
public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String id);

    Mono<Void> deleteById(String id);

    Mono<RecipeCommand> findCommandById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);
}
