package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.Recipe;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Recipe> saveRecipeImage(String recipeId, MultipartFile image);
}
