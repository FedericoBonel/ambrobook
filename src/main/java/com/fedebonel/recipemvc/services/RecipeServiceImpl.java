package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.converters.RecipeCommandToRecipe;
import com.fedebonel.recipemvc.converters.RecipeToRecipeCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Recipe Service Implementation
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Getting recipes");
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        log.debug("Finding recipe by id: " + id);
        return recipeRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.debug("Deleting recipe by id: " + id);
        return recipeRepository.deleteById(id);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        log.debug("Finding command by id: " + id);
        return recipeRepository.findById(id).map(recipe -> {
            RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
            recipeCommand.getIngredients().forEach(ingredientCommand -> ingredientCommand.setRecipeId(recipeCommand.getId()));
            return recipeCommand;
        });
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("Saving recipe command with name: " + recipeCommand.getDescription());

        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        return recipeRepository.findById(recipeCommand.getId())
                .defaultIfEmpty(detachedRecipe)
                .map(recipe -> {
                    detachedRecipe.setImage(recipe.getImage());
                    return detachedRecipe;
                })
                .flatMap(recipeRepository::save)
                .mapNotNull(recipeToRecipeCommand::convert);
    }
}
