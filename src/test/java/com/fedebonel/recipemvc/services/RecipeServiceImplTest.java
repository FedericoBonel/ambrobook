package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.converters.*;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    // Create a mock implementation for the recipe
    @Mock
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository,
                new RecipeCommandToRecipe(
                        new CategoryCommandToCategory(),
                        new NotesCommandToNotes(),
                        new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure())),
                new RecipeToRecipeCommand(
                        new CategoryToCategoryCommand(),
                        new NotesToNotesCommand(),
                        new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand())));
    }

    @Test
    void getRecipesById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Recipe recipeFound = recipeService.findById(1L);

        assertNotNull(recipeFound);
        assertEquals(1L, recipeFound.getId());
        verify(recipeRepository).findById(1L);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        // When the find all gets called in the mock, return the recipesdata
        // If recipes service works fine, this should be whats returned
        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(), recipesData.size());
        // Make sure that the recipes repository findAll only got called ONCE
        // (i.e. when the recipeService.getRecipes() gets called)
        verify(recipeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteById() {
        Long idToDelete = 1L;
        recipeService.deleteById(idToDelete);
        verify(recipeRepository, times(1)).deleteById(idToDelete);
    }
}