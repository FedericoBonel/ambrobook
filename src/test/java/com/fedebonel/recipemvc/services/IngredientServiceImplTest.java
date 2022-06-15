package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.converters.IngredientCommandToIngredient;
import com.fedebonel.recipemvc.converters.IngredientToIngredientCommand;
import com.fedebonel.recipemvc.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.fedebonel.recipemvc.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import com.fedebonel.recipemvc.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeRepository;
    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    IngredientCommandToIngredient converter;
    IngredientServiceImpl ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        ingredientService = new IngredientServiceImpl(recipeRepository,
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                unitOfMeasureRepository,
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()));
    }

    @Test
    void findCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1L");
        recipe.addIngredient(ingredient1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2L");
        recipe.addIngredient(ingredient2);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3L");
        recipe.addIngredient(ingredient3);

        when(recipeRepository.findById(recipe.getId())).thenReturn(Mono.just(recipe));
        IngredientCommand command1 = ingredientService.findCommandById(recipe.getId(), ingredient1.getId()).block();

        assertEquals(ingredient1.getId(), command1.getId());
        assertEquals(recipe.getId(), command1.getRecipeId());
        verify(recipeRepository, times(1)).findById(recipe.getId());
    }

    @Test
    void updateCommand() {
        Mono<Recipe> recipeMono = Mono.just(new Recipe());
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("1L");
        ingredientCommand.setRecipeId("2L");
        Recipe recipe = new Recipe();
        recipe.addIngredient(converter.convert(ingredientCommand));

        when(recipeRepository.findById(anyString())).thenReturn(recipeMono);
        when(recipeRepository.save(any())).thenReturn(Mono.just(recipe));

        IngredientCommand savedIngredient = ingredientService.saveCommand(ingredientCommand).block();

        assertEquals(ingredientCommand.getId(), savedIngredient.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any());
    }
}