package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.commands.RecipeCommand;
import com.fedebonel.recipemvc.services.ImageService;
import com.fedebonel.recipemvc.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {
    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageController = new ImageController(recipeService, imageService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(imageController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    void showUploadImageForm() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);

        when(recipeService.findCommandById(recipe.getId())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + recipe.getId() + "/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/imageform"));
    }

    @Test
    void uploadImage() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt", "text/plain",
                "Test file".getBytes());

        mockMvc.perform(multipart("/recipe/" + recipe.getId() + "/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + recipe.getId() + "/show"));

        verify(imageService).saveRecipeImage(anyLong(), any());
    }

    @Test
    void renderRecipeImage() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);
        String s = "Image text";

        Byte[] image = new Byte[s.getBytes().length];
        int currByte = 0;

        for (byte imageByte : s.getBytes()) {
            image[currByte++] = imageByte;
        }

        recipe.setImage(image);

        when(recipeService.findCommandById(recipe.getId())).thenReturn(recipe);

        // Emulate the response to check that the length of the "image" is the same as the original recipe
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/" + recipe.getId() + "/image/render"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(s.length(), response.getContentAsByteArray().length);
    }

    @Test
    void renderRecipeImageNotExisting() throws Exception {
        mockMvc.perform(get("/recipe/asd/image/render"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/show"));

        verifyNoInteractions(recipeService);
        verifyNoInteractions(imageService);
    }
}