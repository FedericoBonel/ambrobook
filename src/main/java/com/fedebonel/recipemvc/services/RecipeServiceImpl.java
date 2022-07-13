package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.mappers.RecipeDtoToRecipe;
import com.fedebonel.recipemvc.mappers.RecipeToRecipeDto;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.model.User;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Recipe Service Implementation
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeDtoToRecipe recipeDtoToRecipe;
    private final RecipeToRecipeDto recipeToRecipeDto;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeDtoToRecipe recipeDtoToRecipe,
                             RecipeToRecipeDto recipeToRecipeDto,
                             UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeDtoToRecipe = recipeDtoToRecipe;
        this.recipeToRecipeDto = recipeToRecipeDto;
        this.userRepository = userRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Getting recipes");
        HashSet<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
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
    public RecipeDto findCommandById(Long id) {
        log.debug("Finding command by id: " + id);
        return recipeToRecipeDto.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeDto saveRecipeCommand(RecipeDto recipeDto) {
        log.debug("Saving recipe command with name: " + recipeDto.getDescription());
        Recipe detachedRecipe = recipeDtoToRecipe.convert(recipeDto);

        if (detachedRecipe.getId() != null) {
            recipeRepository.findById(detachedRecipe.getId())
                    .ifPresent(recipe -> detachedRecipe.setImage(recipe.getImage()));
        }

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return recipeToRecipeDto.convert(savedRecipe);
    }

    @Override
    public List<Recipe> findByQuery(String query) {
        return recipeRepository.findByDescriptionContainingIgnoreCaseOrCategories_nameContainingIgnoreCase(query, query);
    }

    @Override
    @Transactional
    public void saveRecipeLike(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        Recipe recipe = recipeRepository.findById(id).orElseThrow(NotFoundException::new);

        recipe.likeByUser(foundUser);
        foundUser.likeRecipe(recipe);

        recipeRepository.save(recipe);
        userRepository.save(foundUser);
    }
}
