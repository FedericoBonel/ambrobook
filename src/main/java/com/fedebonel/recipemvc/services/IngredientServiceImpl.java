package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.converters.IngredientCommandToIngredient;
import com.fedebonel.recipemvc.converters.IngredientToIngredientCommand;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public IngredientCommand findCommandById(Long recipeId, Long ingredientId) {
        Recipe foundRecipe = recipeRepository.findById(recipeId).orElse(null);
        if (foundRecipe == null) return null;
        return foundRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(converterToCommand::convert).findFirst().orElse(null);
    }

    @Override
    public IngredientCommand saveCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
        if (recipeOptional.isEmpty()) return new IngredientCommand();

        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> foundIngredient = recipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();
        // We need to do this because the ingredient could exist with its unique id
        // If it exists we need to update its stored instance (since it will share the same id)
        if (foundIngredient.isPresent()){
            Ingredient ingredientFound = foundIngredient.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                    // todo address this:
                    .orElseThrow(() -> new RuntimeException("Unit of measure not found for the ingredient")));
        } else {
            recipe.addIngredient(converterToIngredient.convert(ingredientCommand));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        return converterToCommand.convert(savedRecipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst().get());
    }
}
