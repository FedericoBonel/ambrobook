package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.CategoryService;
import com.fedebonel.recipemvc.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {

    @Mock
    CategoryService categoryService;

    @Mock
    RecipeService recipeService;

    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService, categoryService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(recipeController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    void getRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void getRecipeNotExisting() throws Exception {
        when(recipeService.findById(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/show"));

        verify(recipeService).findById(anyLong());
    }

    @Test
    void getRecipeBadRequest() throws Exception {

        mockMvc.perform(get("/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/show"));

        verifyNoInteractions(recipeService);
    }

    @Test
    void getRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void postRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        // The id won't be generated when the post happens, only after it gets created
                        .param("id", "")
                        .param("description", "This is a description")
                        .param("directions", "Directions..."))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/show"));
    }

    @Test
    void getUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(recipeService.findCommandById(command.getId())).thenReturn(command);

        mockMvc.perform(get("/recipe/" + command.getId() + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void deleteAction() throws Exception {
        long id = 1L;
        mockMvc.perform(get("/recipe/" + id + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

    }
}