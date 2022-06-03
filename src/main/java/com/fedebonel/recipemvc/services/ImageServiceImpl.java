package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    }
}
