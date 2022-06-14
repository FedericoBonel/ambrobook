package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.converters.IngredientCommandToIngredient;
import com.fedebonel.recipemvc.converters.IngredientToIngredientCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand converterToCommand;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient converterToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand converter, UnitOfMeasureRepository unitOfMeasureRepository, IngredientCommandToIngredient converterToIngredient) {
        this.recipeRepository = recipeRepository;
        this.converterToCommand = converter;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converterToIngredient = converterToIngredient;
    }

    @Override
    public IngredientCommand findCommandById(String recipeId, String ingredientId) {
        Recipe foundRecipe = recipeRepository.findById(recipeId).orElse(null);
        if (foundRecipe == null) throw new NotFoundException("Recipe with id = " + recipeId + " not found");

        Optional<IngredientCommand> optionalIngredient = foundRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(converterToCommand::convert).findFirst();

        // Make sure the ingredient is there
        if (optionalIngredient.isEmpty())
            throw new NotFoundException("Ingredient with id = " + ingredientId + " not found");

        // Update it's recipe id
        IngredientCommand ingredientCommand = optionalIngredient.get();
        ingredientCommand.setRecipeId(recipeId);

        // Return it
        return optionalIngredient.get();
    }

    @Override
    public IngredientCommand saveCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
        if (recipeOptional.isEmpty()) return new IngredientCommand();

        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> foundIngredient = recipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (foundIngredient.isPresent()) {
            // Update ingredient
            Ingredient ingredientFound = foundIngredient.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                    .orElseThrow(() -> new NotFoundException("Unit of measure not found for the ingredient")));
        } else {
            // Create ingredient
            Ingredient ingredient = converterToIngredient.convert(ingredientCommand);
            recipe.addIngredient(ingredient);
        }

        // Save/update the ingredient by saving the recipe
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Look for the saved ingredient
        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        // If it doesn't have an id assigned yet
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
        return ingredientCommandSaved;
    }

    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        // Check that the recipe is valid
        if (recipeOptional.isEmpty()) {
            log.debug("Recipe " + recipeId + " for ingredient " + ingredientId + " not found while trying to delete ingredient");
            throw new NotFoundException("Recipe with id = " + recipeId + " not found");
        }

        // Get the ingredient, remove it, and save the recipe again
        Recipe recipe = recipeOptional.get();
        recipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientId))
                .findFirst().ifPresent(recipe::removeIngredient);

        recipeRepository.save(recipe);
    }
}
