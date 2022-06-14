package com.fedebonel.recipemvc.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveRecipeImage(String recipeId, MultipartFile image);
}
