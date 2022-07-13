package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveRecipeImage(Long recipeId, MultipartFile image) {
        log.debug("Saving image for recipe: " + recipeId);

        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null) {
            log.debug("Non existing recipe with id: " + recipeId);
            return;
        }

        // Transform the image to a byte array, assign it to the recipe, and create it
        try {
            Byte[] imageInBytes = new Byte[image.getBytes().length];

            int currByte = 0;
            for (byte imageByte : image.getBytes()) {
                imageInBytes[currByte++] = imageByte;
            }

            recipe.setImage(imageInBytes);
            recipeRepository.save(recipe);

        } catch (IOException e) {
            // TODO show error page
            log.debug("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
