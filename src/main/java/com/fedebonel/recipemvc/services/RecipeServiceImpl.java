package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.converters.RecipeCommandToRecipe;
import com.fedebonel.recipemvc.converters.RecipeToRecipeCommand;
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
    public Set<Recipe> getRecipes() {
        log.debug("I'm in the service");
        HashSet<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(Long id) {
        log.debug("finding recipe by id: " + id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElse(null);
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("Saving recipe command: " + recipeCommand.getId());
        // Still a pojo not yet in hibernate context, that's why is detached
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        // Save it in the database (if exists it will update it where necessary)
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        log.debug("Finding command by id: " + id);
        return recipeToRecipeCommand.convert(findById(id));
    }
}
