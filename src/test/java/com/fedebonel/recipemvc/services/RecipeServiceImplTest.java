package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.converters.*;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    // Create a mock implementation for the recipe
    @Mock
    RecipeReactiveRepository recipeRepository;

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
        recipe.setId("1L");

        when(recipeRepository.findById("1L")).thenReturn(Mono.just(recipe));

        Recipe recipeFound = recipeService.findById("1L").block();

        assertNotNull(recipeFound);
        assertEquals("1L", recipeFound.getId());
        verify(recipeRepository).findById("1L");
    }

    @Test
    void getRecipesByIdNotExisting() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeRepository.findById("1L")).thenReturn(Mono.empty());

        assertEquals(Mono.empty().block(), recipeService.findById("1L").block());
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        Flux<Recipe> recipesData = Flux.just(recipe);

        // When the find all gets called in the mock, return the recipesdata
        // If recipes service works fine, this should be whats returned
        when(recipeRepository.findAll()).thenReturn(recipesData);

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertNotNull(recipes);
        assertEquals(recipes.size(), 1);
        // Make sure that the recipes repository findAll only got called ONCE
        // (i.e. when the recipeService.getRecipes() gets called)
        verify(recipeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteById() {
        String idToDelete = "1L";
        when(recipeRepository.deleteById(idToDelete)).thenReturn(Mono.empty());
        recipeService.deleteById(idToDelete).block();
        verify(recipeRepository, times(1)).deleteById(idToDelete);
    }
}