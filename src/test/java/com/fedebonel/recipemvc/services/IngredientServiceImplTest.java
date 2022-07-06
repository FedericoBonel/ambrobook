package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.mappers.IngredientDtoToIngredient;
import com.fedebonel.recipemvc.mappers.IngredientToIngredientDto;
import com.fedebonel.recipemvc.mappers.UnitOfMeasureDtoToUnitOfMeasure;
import com.fedebonel.recipemvc.mappers.UnitOfMeasureToUnitOfMeasureDto;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import com.fedebonel.recipemvc.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientDtoToIngredient converter;
    IngredientServiceImpl ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        converter = new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure());
        ingredientService = new IngredientServiceImpl(recipeRepository,
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()),
                unitOfMeasureRepository,
                new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()));
    }

    @Test
    void findCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        recipe.addIngredient(ingredient1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        recipe.addIngredient(ingredient2);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        recipe.addIngredient(ingredient3);

        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        IngredientDto command1 = ingredientService.findCommandById(recipe.getId(), ingredient1.getId());

        assertEquals(ingredient1.getId(), command1.getId());
        assertEquals(recipe.getId(), command1.getRecipeId());
        verify(recipeRepository, times(1)).findById(recipe.getId());
    }

    @Test
    void updateCommand() {
        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        ingredientDto.setRecipeId(2L);
        Recipe recipe = new Recipe();
        recipe.addIngredient(converter.convert(ingredientDto));

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(recipe);

        IngredientDto savedIngredient = ingredientService.saveCommand(ingredientDto);

        assertEquals(ingredientDto.getId(), savedIngredient.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any());
    }
}