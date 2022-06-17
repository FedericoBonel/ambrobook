package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.model.Recipe;
import com.fedebonel.recipemvc.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Assigns the image to the recipe and persists it in database
     */
    @Override
    public Mono<Recipe> saveRecipeImage(String recipeId, FilePart image) {
        log.debug("Saving image for recipe: " + recipeId);

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe with id: " + recipeId + " not found")))
                .zipWith(DataBufferUtils.join(image.content()))
                .map(recipeAndImage -> {
                    recipeAndImage.getT1().setImage(ArrayUtils.toObject(recipeAndImage.getT2().asByteBuffer().array()));
                    return recipeAndImage.getT1();
                })
                .flatMap(recipeRepository::save);
    }
}
