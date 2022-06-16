package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.converters.IngredientCommandToIngredient;
import com.fedebonel.recipemvc.converters.IngredientToIngredientCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import com.fedebonel.recipemvc.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeReactiveRepository recipeRepository;
    private final IngredientToIngredientCommand converterToCommand;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient converterToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeRepository, IngredientToIngredientCommand converter,
                                 UnitOfMeasureReactiveRepository unitOfMeasureRepository, IngredientCommandToIngredient converterToIngredient) {
        this.recipeRepository = recipeRepository;
        this.converterToCommand = converter;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converterToIngredient = converterToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findCommandById(String recipeId, String ingredientId) {

        return recipeRepository
                // Find recipe as single (mono) data stream
                .findById(recipeId)
                // Once recipe gets in stream execute this / transform it and return it as a multi/flux data stream
                // (get its ingredients)
                .flatMapIterable(Recipe::getIngredients)
                // Filter from the data stream the ingredient that we need
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                // Make sure we are getting ONE ingredient (Otherwise throw an error) and return it as a mono
                .single()
                // Map it to command and set its recipe id since it won't store it in the database document and
                // return it inside of a mono
                .map(ingredient -> {
                    IngredientCommand ingredientCommand = converterToCommand.convert(ingredient);
                    ingredientCommand.setRecipeId(recipeId);
                    return ingredientCommand;
                });
    }

    @Override
    public Mono<IngredientCommand> saveCommand(IngredientCommand ingredientCommand) {

        // Convert the ingredient to save
        Ingredient ingredientToSave = converterToIngredient.convert(ingredientCommand);

        // Assign its unit of measure, find its recipe
        return unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                .map(ingredientAndUom -> {
                    ingredientToSave.setUom(ingredientAndUom);
                    return ingredientToSave;
                })
                .flatMap(ingredient -> recipeRepository.findById(ingredientCommand.getRecipeId()))
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id " + ingredientCommand.getRecipeId() + " not found")))
                .map(ingredientRecipe -> {
                    ingredientRecipe.removeIngredient(ingredientToSave);
                    ingredientRecipe.addIngredient(ingredientToSave);
                    return ingredientRecipe;
                })
                .flatMap(recipeRepository::save)
                .mapNotNull(recipe -> recipe.getIngredients()
                        .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                        .findFirst().orElse(null))
                .mapNotNull(converterToCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId).share().blockOptional();
        // Check that the recipe is valid
        if (recipeOptional.isEmpty()) {
            log.debug("Recipe " + recipeId + " for ingredient " + ingredientId + " not found while trying to delete ingredient");
            throw new NotFoundException("Recipe with id = " + recipeId + " not found");
        }

        // Get the ingredient, remove it, and save the recipe again
        Recipe recipe = recipeOptional.get();
        recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId));

        recipeRepository.save(recipe).share().block();

        return Mono.empty();
    }
}
