package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.IngredientCommand;
import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.commands.UnitOfMeasureCommand;
import com.fedebonel.recipemvc.model.UnitOfMeasure;
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
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.findCommandById(recipeCommand.getId())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/" + recipeCommand.getId() + "/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void showIngredient() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId(1L);
        when(ingredientService.findCommandById(anyLong(), anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/ingredient/" + command.getId() + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void updateRecipeIngredient() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId(1L);
        UnitOfMeasureCommand uom1 = new UnitOfMeasureCommand();
        uom1.setId(2L);
        Set<UnitOfMeasureCommand> uoms = new HashSet<>();
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
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        ingredientCommand.setRecipeId(2L);
        when(ingredientService.saveCommand(any())).thenReturn(ingredientCommand);

        mockMvc.perform(post("/recipe/" + ingredientCommand.getRecipeId() + "/ingredient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view()
                        .name("redirect:/recipe/"
                                + ingredientCommand.getRecipeId()
                                +  "/ingredient/"
                                + ingredientCommand.getId() + "/show"));
    }


}