package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    // IndexController to test
    IndexController indexController;

    @Mock
    RecipeService recipeService;
    @Mock
    Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        indexController = new IndexController(recipeService);
    }

    /* Controller test with HTTP GET request with MockMvcVuilders */
    @Test
    void testoMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getIndexPage() {
        // Given
        String expectedViewName = "index";

        HashSet<Recipe> expectedSet = new HashSet<>();
        expectedSet.add(new Recipe());
        when(recipeService.getRecipes()).thenReturn(expectedSet);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        // When
        String actualViewName = indexController.getIndexPage(model);

        // Then
        assertEquals(expectedViewName, actualViewName);

        // Verify interactions in the mocks only happens ONCE
        verify(recipeService, times(1)).getRecipes();
        // Verify specifically that the model has a string that is equal (eq) to "recipes" and the
        // recipes set that we have set for the recipes service at the beginning of the test
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        assertEquals(expectedSet.size(), argumentCaptor.getValue().size());
    }
}