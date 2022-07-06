package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveRecipeImage() throws IOException {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt", "text/plain",
                "Test file".getBytes());

        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        imageService.saveRecipeImage(recipe.getId(), multipartFile);

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);

        // Capture the recipe that got saved inside the service
        verify(recipeRepository).save(recipeCaptor.capture());

        // Verify that the saved image has the same length that the one we wanted to save
        Recipe savedRecipe = recipeCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}