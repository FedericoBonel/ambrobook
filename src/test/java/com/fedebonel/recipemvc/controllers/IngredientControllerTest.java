package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.datatransferobjects.IngredientDto;
import com.fedebonel.recipemvc.datatransferobjects.RecipeDto;
import com.fedebonel.recipemvc.datatransferobjects.UnitOfMeasureDto;
import com.fedebonel.recipemvc.model.Ingredient;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.IngredientService;
import com.fedebonel.recipemvc.services.RecipeService;
import com.fedebonel.recipemvc.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {

    @Mock
    IngredientService ingredientService;

    @Mock
    RecipeService recipeService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;

    IngredientController ingredientController;


    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void testListIngredients() throws Exception {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        when(recipeService.findCommandById(recipeDto.getId())).thenReturn(recipeDto);

        mockMvc.perform(get("/recipe/" + recipeDto.getId() + "/ingredient"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void showIngredient() throws Exception {
        IngredientDto command = new IngredientDto();
        command.setId(1L);
        when(ingredientService.findCommandById(anyLong(), anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/ingredient/" + command.getId() + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void createRecipeIngredientForm() throws Exception {
        RecipeDto recipe = new RecipeDto();
        recipe.setId(1L);

        when(recipeService.findCommandById(recipe.getId())).thenReturn(recipe);
        when(unitOfMeasureService.listAllUOM()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/" + recipe.getId() + "/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient", "uomList"));

        verify(unitOfMeasureService).listAllUOM();
    }

    @Test
    void updateRecipeIngredientForm() throws Exception {
        IngredientDto command = new IngredientDto();
        command.setId(1L);
        UnitOfMeasureDto uom1 = new UnitOfMeasureDto();
        uom1.setId(2L);
        Set<UnitOfMeasureDto> uoms = new HashSet<>();
        uoms.add(uom1);
        when(ingredientService.findCommandById(anyLong(), anyLong())).thenReturn(command);
        when(unitOfMeasureService.listAllUOM()).thenReturn(uoms);

        mockMvc.perform(get("/recipe/1/ingredient/" + command.getId() + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient", "uomList"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        ingredientDto.setRecipeId(2L);
        when(ingredientService.saveCommand(any())).thenReturn(ingredientDto);

        mockMvc.perform(post("/recipe/" + ingredientDto.getRecipeId() + "/ingredient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view()
                        .name("redirect:/recipe/"
                                + ingredientDto.getRecipeId()
                                + "/ingredient"));
    }

    @Test
    void deleteRecipeIngredient() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);

        mockMvc.perform(get("/recipe/" + recipe.getId() + "/ingredient/" + ingredient.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + recipe.getId() + "/ingredient"));

        verify(ingredientService).deleteById(anyLong(), anyLong());
    }
}