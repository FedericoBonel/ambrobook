package com.fedebonel.recipemvc.mappers;

import com.fedebonel.recipemvc.datatransferobjects.CategoryDto;
import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.datatransferobjects.NotesDto;
import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.model.Difficulty;
import com.fedebonel.recipemvc.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeDtoToRecipeTest {
    public static final Long RECIPE_ID = 1L;
    public static final Integer COOK_TIME = Integer.valueOf("5");
    public static final Integer PREP_TIME = Integer.valueOf("7");
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf("3");
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID2 = 2L;
    public static final Long INGRED_ID_1 = 3L;
    public static final Long INGRED_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;

    RecipeDtoToRecipe converter;


    @BeforeEach
    public void setUp() throws Exception {
        converter = new RecipeDtoToRecipe(new CategoryDtoToCategory(),
                new NotesDtoToNotes(),
                new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()));
    }

    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new RecipeDto()));
    }

    @Test
    public void convert() throws Exception {
        //given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(RECIPE_ID);
        recipeDto.setCookTime(COOK_TIME);
        recipeDto.setPrepTime(PREP_TIME);
        recipeDto.setDescription(DESCRIPTION);
        recipeDto.setDifficulty(DIFFICULTY);
        recipeDto.setDirections(DIRECTIONS);
        recipeDto.setServings(SERVINGS);
        recipeDto.setSources(SOURCE);
        recipeDto.setUrl(URL);

        NotesDto notes = new NotesDto();
        notes.setId(NOTES_ID);

        recipeDto.setNotes(notes);

        CategoryDto category = new CategoryDto();
        category.setId(CAT_ID_1);

        CategoryDto category2 = new CategoryDto();
        category2.setId(CAT_ID2);

        recipeDto.getCategories().add(category);
        recipeDto.getCategories().add(category2);

        IngredientDto ingredient = new IngredientDto();
        ingredient.setId(INGRED_ID_1);

        IngredientDto ingredient2 = new IngredientDto();
        ingredient2.setId(INGRED_ID_2);

        recipeDto.getIngredients().add(ingredient);
        recipeDto.getIngredients().add(ingredient2);

        //when
        Recipe recipe = converter.convert(recipeDto);

        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(NOTES_ID, recipe.getNote().getId());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
    }

}