package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.converters.RecipeCommandToRecipe;
import com.fedebonel.recipemvc.converters.RecipeToRecipeCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Recipe Service Implementation
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    @Transactional
    public Set<Recipe> getRecipes() {
        log.debug("Getting recipes");
        HashSet<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    @Transactional
    public Recipe findById(Long id) {
        log.debug("Finding recipe by id: " + id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isEmpty()) {
            throw new NotFoundException("Recipe with id = " + id + " not found");
        }

        return optionalRecipe.orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting recipe by id: " + id);
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        log.debug("Finding command by id: " + id);
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("Saving recipe command with name: " + recipeCommand.getDescription());
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        if (detachedRecipe.getId() != null) {
            recipeRepository.findById(detachedRecipe.getId())
                    .ifPresent(recipe -> {
                        detachedRecipe.setImage(recipe.getImage());
                    });
        }

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return recipeToRecipeCommand.convert(savedRecipe);
    }
}
