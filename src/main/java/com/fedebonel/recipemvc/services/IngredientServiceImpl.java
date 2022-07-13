package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.mappers.IngredientDtoToIngredient;
import com.fedebonel.recipemvc.mappers.IngredientToIngredientDto;
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
    private final IngredientToIngredientDto converterToCommand;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientDtoToIngredient converterToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientDto converter, UnitOfMeasureRepository unitOfMeasureRepository, IngredientDtoToIngredient converterToIngredient) {
        this.recipeRepository = recipeRepository;
        this.converterToCommand = converter;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converterToIngredient = converterToIngredient;
    }

    @Override
    public IngredientDto findCommandById(Long recipeId, Long ingredientId) {
        Recipe foundRecipe = recipeRepository.findById(recipeId).orElse(null);
        if (foundRecipe == null) throw new NotFoundException("Recipe with id = " + recipeId + " not found");
        return foundRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(converterToCommand::convert).findFirst().orElse(null);
    }

    @Override
    public IngredientDto saveCommand(IngredientDto ingredientDto) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientDto.getRecipeId());
        if (recipeOptional.isEmpty()) return new IngredientDto();
        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> foundIngredient = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientDto.getId()))
                .findFirst();
        // We need to do this because the ingredient could exist with its unique id
        // If it exists we need to update its stored instance (since it will share the same id)
        if (foundIngredient.isPresent()) {
            // Update ingredient
            Ingredient ingredientFound = foundIngredient.get();
            ingredientFound.setDescription(ingredientDto.getDescription());
            ingredientFound.setAmount(ingredientDto.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientDto.getUom().getId())
                    .orElseThrow(() -> new NotFoundException("Unit of measure not found for the ingredient")));
        } else {
            // Create ingredient
            Ingredient ingredient = converterToIngredient.convert(ingredientDto);
            recipe.addIngredient(ingredient);
        }

        // Save/update the ingredient by saving the recipe
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Look for the saved ingredient
        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientDto.getId()))
                .findFirst();

        // If it doesn't have an id assigned yet
        if (savedIngredient.isEmpty()) {
            savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equals(ingredientDto.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(ingredientDto.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(ingredientDto.getUom().getId()))
                    .findFirst();
        }

        // return it
        return converterToCommand.convert(savedIngredient.get());
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        // Check that the recipe is valid
        if (recipeOptional.isEmpty()) {
            log.debug("Recipe " + recipeId + " for ingredient " + ingredientId + " not found while trying to delete ingredient");
            throw new NotFoundException("Recipe with id = " + recipeId + " not found");
        }
        // Get the ingredient, remove it, and create the recipe again
        Recipe recipe = recipeOptional.get();
        recipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientId))
                .findFirst().ifPresent(recipe::removeIngredient);

        recipeRepository.save(recipe);
    }
}
