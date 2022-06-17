package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.exceptions.NotFoundException;
import com.fedebonel.recipemvc.services.ImageService;
import com.fedebonel.recipemvc.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    /**
     * Handles GET requests to show the image form to upload new recipe images
     */
    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadImageForm(@PathVariable String recipeId, Model model) {
        log.debug("Showing image form for recipe: " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        return "recipe/imageform";
    }

    /**
     * Handles POST requests to upload new recipe images
     */
    @PostMapping("/recipe/{recipeId}/image")
    public Mono<String> uploadImage(@PathVariable String recipeId,
                                    @RequestPart("imagefile") Mono<FilePart> image) {

        log.debug("Uploading an image for recipe: " + recipeId);

        return image.map(imageToSave -> imageToSave)
                .flatMap(imageToSave -> imageService.saveRecipeImage(recipeId, imageToSave))
                .thenReturn("redirect:/recipe/" + recipeId + "/show");
    }

    /**
     * Handles GET requests to render the recipe image
     */
    @GetMapping(value = "/recipe/{recipeId}/image/render", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<Void> renderRecipeImage(@PathVariable String recipeId, ServerHttpResponse response) {

        return recipeService.findById(recipeId)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe not found in database!")))
                .map(recipe -> response.bufferFactory().wrap(ArrayUtils.toPrimitive(recipe.getImage())))
                .flatMap(image -> response.writeWith(Flux.just(image)));
    }
}
