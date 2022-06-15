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
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId()).blockOptional();
        if (recipeOptional.isEmpty()) return Mono.just(new IngredientCommand());

        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> foundIngredient = recipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (foundIngredient.isPresent()) {
            // If it exists update ingredient
            Ingredient ingredientFound = foundIngredient.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository
                    .findById(ingredientCommand.getUom().getId()).blockOptional()
                    .orElseThrow(() -> new NotFoundException("Unit of measure not found for the ingredient")));
        } else {
            // Otherwise, create ingredient
            recipe.addIngredient(converterToIngredient.convert(ingredientCommand));
        }

        // Save/update the ingredient by saving the recipe
        Recipe savedRecipe = recipeRepository.save(recipe).block();

        // Look for the saved ingredient
        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        // If it doesn't have an id assigned yet because is a new ingredient
        if (savedIngredient.isEmpty()) {
            savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equals(ingredientCommand.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(ingredientCommand.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(ingredientCommand.getUom().getId()))
                    .findFirst();
        }

        // Make sure the recipe id is getting set in the saved instance
        IngredientCommand ingredientCommandSaved = converterToCommand.convert(savedIngredient.get());
        ingredientCommandSaved.setRecipeId(recipe.getId());

        // return it
        return Mono.just(ingredientCommandSaved);
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId).blockOptional();
        // Check that the recipe is valid
        if (recipeOptional.isEmpty()) {
            log.debug("Recipe " + recipeId + " for ingredient " + ingredientId + " not found while trying to delete ingredient");
            throw new NotFoundException("Recipe with id = " + recipeId + " not found");
        }

        // Get the ingredient, remove it, and save the recipe again
        Recipe recipe = recipeOptional.get();
        recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId));

        recipeRepository.save(recipe).block();

        return Mono.empty();
    }
}
