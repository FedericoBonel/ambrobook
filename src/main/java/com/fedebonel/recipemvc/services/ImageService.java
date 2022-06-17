package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.Recipe;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Recipe> saveRecipeImage(String recipeId, FilePart image);
}
